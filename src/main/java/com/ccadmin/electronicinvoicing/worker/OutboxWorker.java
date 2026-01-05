package com.ccadmin.electronicinvoicing.worker;

import com.ccadmin.electronicinvoicing.config.ApplicationProperties;
import com.ccadmin.electronicinvoicing.entity.OutboxEntity;
import com.ccadmin.electronicinvoicing.entity.OutboxStatus;
import com.ccadmin.electronicinvoicing.repository.OutboxRepository;
import com.ccadmin.electronicinvoicing.service.impl.ElectronicInvoicingServiceImpl;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class OutboxWorker {
    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ElectronicInvoicingServiceImpl electronicInvoicingService;

    @Scheduled(fixedDelayString = "${app.outbox.fixed-delay-ms:15000}")
    @Transactional
    public void processOutbox() {
        List<OutboxEntity> pending = outboxRepository.findTop20ByStatusAndNextRetryAtBeforeOrderByNextRetryAtAsc(
            OutboxStatus.PENDING, OffsetDateTime.now());
        for (OutboxEntity outbox : pending) {
            try {
                outbox.setStatus(OutboxStatus.PROCESSING);
                outbox.setUpdatedAt(OffsetDateTime.now());
                outboxRepository.save(outbox);
                electronicInvoicingService.processOutbox(outbox);
                outbox.setStatus(OutboxStatus.DONE);
                outbox.setUpdatedAt(OffsetDateTime.now());
                outboxRepository.save(outbox);
            } catch (Exception ex) {
                int attempts = outbox.getAttempts() + 1;
                outbox.setAttempts(attempts);
                outbox.setLastError(ex.getMessage());
                if (attempts >= applicationProperties.getOutbox().getMaxAttempts()) {
                    outbox.setStatus(OutboxStatus.FAILED);
                } else {
                    outbox.setStatus(OutboxStatus.PENDING);
                    outbox.setNextRetryAt(OffsetDateTime.now().plusSeconds(30L * attempts));
                }
                outbox.setUpdatedAt(OffsetDateTime.now());
                outboxRepository.save(outbox);
            }
        }
    }
}

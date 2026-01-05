package com.ccadmin.electronicinvoicing.repository;

import com.ccadmin.electronicinvoicing.entity.OutboxEntity;
import com.ccadmin.electronicinvoicing.entity.OutboxStatus;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxRepository extends JpaRepository<OutboxEntity, String> {
    List<OutboxEntity> findTop20ByStatusAndNextRetryAtBeforeOrderByNextRetryAtAsc(OutboxStatus status, OffsetDateTime now);
}

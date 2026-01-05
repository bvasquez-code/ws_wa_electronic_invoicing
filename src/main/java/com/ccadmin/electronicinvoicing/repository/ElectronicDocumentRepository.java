package com.ccadmin.electronicinvoicing.repository;

import com.ccadmin.electronicinvoicing.entity.ElectronicDocumentEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ElectronicDocumentRepository extends JpaRepository<ElectronicDocumentEntity, String> {
    Optional<ElectronicDocumentEntity> findById(String id);

    List<ElectronicDocumentEntity> findBySunatTicket(String sunatTicket);
}

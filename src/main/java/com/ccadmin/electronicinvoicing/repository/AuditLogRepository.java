package com.ccadmin.electronicinvoicing.repository;

import com.ccadmin.electronicinvoicing.entity.AuditLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditLogRepository extends JpaRepository<AuditLogEntity, String> {
}

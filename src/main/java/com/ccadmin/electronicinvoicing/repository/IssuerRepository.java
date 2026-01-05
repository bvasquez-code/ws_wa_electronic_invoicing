package com.ccadmin.electronicinvoicing.repository;

import com.ccadmin.electronicinvoicing.entity.IssuerEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IssuerRepository extends JpaRepository<IssuerEntity, String> {
    Optional<IssuerEntity> findByRuc(String ruc);
}

package com.ccadmin.electronicinvoicing.repository;

import com.ccadmin.electronicinvoicing.entity.FileStoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileStoreRepository extends JpaRepository<FileStoreEntity, String> {
}

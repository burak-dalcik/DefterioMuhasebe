package com.defterio.repository;

import com.defterio.entity.Attachment;
import com.defterio.entity.OwnerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findByOwnerTypeAndOwnerId(OwnerType ownerType, Long ownerId);
}

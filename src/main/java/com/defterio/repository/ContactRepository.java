package com.defterio.repository;

import com.defterio.entity.Contact;
import com.defterio.entity.ContactType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    
    @Query("SELECT c FROM Contact c WHERE " +
           "(:query IS NULL OR LOWER(c.name) LIKE LOWER(CONCAT('%', :query, '%'))) AND " +
           "(:type IS NULL OR c.type = :type)")
    Page<Contact> findByQueryAndType(@Param("query") String query, 
                                     @Param("type") ContactType type, 
                                     Pageable pageable);
}

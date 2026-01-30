package com.defterio.service;

import com.defterio.dto.ContactRequest;
import com.defterio.dto.ContactResponse;
import com.defterio.entity.Contact;
import com.defterio.entity.ContactType;
import com.defterio.exception.ConflictException;
import com.defterio.exception.NotFoundException;
import com.defterio.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactRepository contactRepository;

    public Page<ContactResponse> findAll(String query, ContactType type, Pageable pageable) {
        Page<Contact> contacts = contactRepository.findByQueryAndType(query, type, pageable);
        return contacts.map(this::toResponse);
    }

    public long countSuppliers() {
        return contactRepository.countByType(ContactType.SUPPLIER);
    }

    public ContactResponse findById(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Contact not found"));
        return toResponse(contact);
    }

    @Transactional
    public ContactResponse create(ContactRequest request) {
        Contact contact = Contact.builder()
                .type(request.getType())
                .name(request.getName())
                .taxNo(request.getTaxNo())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .build();

        contact = contactRepository.save(contact);
        return toResponse(contact);
    }

    @Transactional
    public ContactResponse update(Long id, ContactRequest request) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Contact not found"));

        contact.setType(request.getType());
        contact.setName(request.getName());
        contact.setTaxNo(request.getTaxNo());
        contact.setPhone(request.getPhone());
        contact.setEmail(request.getEmail());
        contact.setAddress(request.getAddress());

        contact = contactRepository.save(contact);
        return toResponse(contact);
    }

    @Transactional
    public void delete(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Contact not found"));

        if (hasTransactions(contact.getId())) {
            throw new ConflictException("Cannot delete contact with existing transactions");
        }

        contactRepository.delete(contact);
    }

    private boolean hasTransactions(Long contactId) {
        return false;
    }

    private ContactResponse toResponse(Contact contact) {
        return ContactResponse.builder()
                .id(contact.getId())
                .type(contact.getType())
                .name(contact.getName())
                .taxNo(contact.getTaxNo())
                .phone(contact.getPhone())
                .email(contact.getEmail())
                .address(contact.getAddress())
                .createdAt(contact.getCreatedAt())
                .updatedAt(contact.getUpdatedAt())
                .build();
    }
}

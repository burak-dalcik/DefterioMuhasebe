package com.defterio.service;

import com.defterio.dto.AccountRequest;
import com.defterio.dto.AccountResponse;
import com.defterio.entity.Account;
import com.defterio.exception.ConflictException;
import com.defterio.exception.NotFoundException;
import com.defterio.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public Page<AccountResponse> findAll(Pageable pageable) {
        Page<Account> accounts = accountRepository.findAll(pageable);
        return accounts.map(this::toResponse);
    }

    public AccountResponse findById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));
        return toResponse(account);
    }

    @Transactional
    public AccountResponse create(AccountRequest request) {
        Account account = Account.builder()
                .type(request.getType())
                .name(request.getName())
                .currency(request.getCurrency() != null ? request.getCurrency() : "TRY")
                .build();

        account = accountRepository.save(account);
        return toResponse(account);
    }

    @Transactional
    public AccountResponse update(Long id, AccountRequest request) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        account.setType(request.getType());
        account.setName(request.getName());
        if (request.getCurrency() != null) {
            account.setCurrency(request.getCurrency());
        }

        account = accountRepository.save(account);
        return toResponse(account);
    }

    @Transactional
    public void delete(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Account not found"));

        if (hasTransactions(account.getId())) {
            throw new ConflictException("Cannot delete account with existing transactions");
        }

        accountRepository.delete(account);
    }

    private boolean hasTransactions(Long accountId) {
        return false;
    }

    private AccountResponse toResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .type(account.getType())
                .name(account.getName())
                .currency(account.getCurrency())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}

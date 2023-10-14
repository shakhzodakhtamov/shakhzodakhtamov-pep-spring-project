package com.example.service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public Account registerAccount(Account account) {
        // Check if the username is not blank
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return null;
        }

        // Check if the password is at least 4 characters long
        if (account.getPassword() == null || account.getPassword().length() < 4) {
            return null;
        }

        // Check if an Account with the same username already exists
        Account existingAccount = accountRepository.findByUsername(account.getUsername());
        if (existingAccount != null) {
            return null; // Duplicate username, return null or handle duplicate username error as needed
        }

        // If all conditions are met, save the account
        return accountRepository.save(account);
    }
    @Transactional
    public Account authenticateAccount(Account account) {
        // Check if the provided username and password match an existing account
        Account existingAccount = accountRepository.findByUsername(account.getUsername());

        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount; // Authentication successful
        }

        return null; // Authentication failed
    }
    @Transactional
    public Account getAccountById(Integer accountId) {
        return accountRepository.findById(accountId).orElse(null);
    }
}


package com.test.main.service;

import com.test.main.model.Account;
import com.test.main.repository.Repository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private AccountService accountService;
    @Mock
    private Repository<Account, Long> repository;
    private Account account;

    @BeforeEach
    void init() {
        account = new Account();
        account.setId(1L);
        account.setAmount(35D);
        accountService = new AccountService(repository);
    }

    @Test
    @DisplayName("Add: test add is successfull")
    void testGetIsSuccessful() {
        accountService.add(account);
        verify(repository, times(1)).add(account);
    }

    @Test
    @DisplayName("Add: test add with invalid data")
    void testAddWithInvalidData() {
        Account add = accountService.add(null);

        verify(repository, times(0)).add(any());
        Assertions.assertNull(add);
    }

    @Test
    @DisplayName("Add: test add with invalid amount")
    void testAddWithInvalidAmount() {
        account.setAmount(null);
        Account add = accountService.add(account);

        verify(repository, times(0)).add(any());
        Assertions.assertNull(add);
    }

    @Test
    @DisplayName("Get: test get is valid")
    void testGetIsValid() {
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        accounts.add(account);
        accounts.add(account);
        when(repository.get()).thenReturn(accounts);
        List<Account> accountList = accountService.get();

        verify(repository, times(1)).get();
        Assertions.assertEquals(accountList, accounts);
    }

    @Test
    @DisplayName("Get: test get is valid with empty list")
    void testGetIsValidWithEmptyList() {
        when(repository.get()).thenReturn(Collections.emptyList());
        List<Account> accountList = accountService.get();

        verify(repository, times(1)).get();
        Assertions.assertIterableEquals(accountList, Collections.emptyList());
    }

    @Test
    @DisplayName("Update: test update is valid")
    void testUpdateIsValid() {
        account.setAmount(20D);
        when(repository.update(1L, account)).thenReturn(account);
        Account add = accountService.update("1", account);

        verify(repository, times(1)).update(eq(1L), eq(account));
        Assertions.assertEquals(add, account);
    }

    @Test
    @DisplayName("Update: test update is invalid")
    void testUpdateIsInValid() {
        Account update = accountService.update("1", null);

        verify(repository, times(0)).add(null);
        Assertions.assertNull(update);
    }

    @Test
    @DisplayName("Update: test update is invalid with wrong amount")
    void testUpdateWithWrongAmount() {
        account.setAmount(null);
        Account update = accountService.update("1", account);

        verify(repository, times(0)).add(account);
        Assertions.assertNull(update);
    }

    @Test
    @DisplayName("Delete: test delete is valid")
    void testDeleteIsValid() {
        when(repository.delete(1L)).thenReturn(account);
        Account update = accountService.delete("1");

        verify(repository, times(1)).delete(1L);

        Assertions.assertEquals(update, account);
    }

}

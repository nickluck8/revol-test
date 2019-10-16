package com.test.main.service;

import com.test.main.model.Account;
import com.test.main.repository.Repository;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AccountService implements Service<Account, String> {
    private final static Logger logger = LoggerFactory.getLogger(AccountService.class);
    private Repository<Account, Long> repository;

    public AccountService() {
    }

    public AccountService(Repository repository) {
        this.repository = repository;
    }

    @Override
    public List<Account> get() {
        logger.debug("-- loading persons --");
        List<Account> accounts = repository.getAll();
        return Objects.nonNull(accounts) ? accounts : Collections.emptyList();
    }

    @Override
    public Account add(Account account) {
        if (!accountIsValid(account)) {
            return null;
        }
        logger.debug("-- persisting persons --");
        logger.debug(account.toString());
        return repository.add(account);
    }

    private boolean accountIsValid(Account account) {
        return Objects.nonNull(account) && account.getAmount() != null;
    }

    @Override
    public Account update(String id, Account account) {
        logger.debug("Updating {}", id);
        if (!accountIsValid(account)) {
            return null;
        }
        Long accountId = Long.valueOf(id);
        return repository.update(accountId, account);
    }

    @Override
    public Account delete(String id) {
        logger.debug("Deleting {}", id);
        Long accountId = Long.valueOf(id);

        return repository.delete(accountId);
    }

    @Override
    public void transferMoney(String id, String toAccountId, Double amount) {
        Long fromAccountId = Long.valueOf(id);
        Long parsedToAccountId = Long.valueOf(toAccountId);

        Account fromAccount = repository.getById(fromAccountId);
        Account toAccount = repository.getById(parsedToAccountId);
        if (Objects.isNull(fromAccount) || Objects.isNull(toAccount)) {
            throw new IllegalArgumentException("Wrong account(s)");
        }
        Object monitor1 = fromAccount.getId() < toAccount.getId() ? fromAccount : toAccount;
        Object monitor2 = fromAccount.getId() > toAccount.getId() ? fromAccount : toAccount;

        synchronized (monitor1) {
            synchronized (monitor2) {
                if (fromAccount.getAmount() < amount) {
                    throw new IllegalArgumentException(
                            String.format("Not enough amount for account %s, available: %s, required: %s",
                                    id, fromAccount.getAmount(), amount));
                }

                if (fromAccount.getId().equals(toAccount.getId())) {
                    throw new IllegalArgumentException("Accounts must be different");
                }

                fromAccount.setAmount(fromAccount.getAmount() - amount);
                toAccount.setAmount(toAccount.getAmount() + amount);
                repository.transferMoney(fromAccount, toAccount);
            }
        }


    }

}

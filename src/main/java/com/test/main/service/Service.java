package com.test.main.service;

import java.util.List;

public interface Service<T, R> {
    List<T> get();

    T add(T t);

    T update(R id, T t);

    T delete(R id);

    void transferMoney(String id, String toAccount, Double amount);
}

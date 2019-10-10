package com.test.main.repository;

import java.util.List;

public interface Repository<T, R> {
    List<T> get();

    T add(T entity);

    T update(R id, T entity);

    T delete(R id);
}

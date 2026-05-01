package com.gamestudio.data.service;

import java.util.List;

public interface ServiceTest<T, U> {
    T getEntity();

    void findByIdTest();

    void deleteByIdTest();

    List<U> getAll();
}

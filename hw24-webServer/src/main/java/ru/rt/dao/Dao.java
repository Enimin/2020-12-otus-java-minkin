package ru.rt.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(Long id);
    T save(T t);
    List<T> findAll();
}
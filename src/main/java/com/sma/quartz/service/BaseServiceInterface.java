package com.sma.quartz.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Collection;
import java.util.Optional;

@SuppressWarnings("PMD")
public interface BaseServiceInterface<T> {


    Collection<T> getAll();


    Page<T> getPage(PageRequest pageable, T filterDomain);

    Page<T> getPage(PageRequest pageable);

    Optional<T> findById(Long id);

    void delete(Long id);

    T create(T item);

    T update(Long id, T item);


}

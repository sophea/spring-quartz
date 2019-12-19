package com.sma.quartz.service;

import com.sma.common.tools.exceptions.ItemNotFoundBusinessException;
import com.sma.quartz.entity.AbstractLongDomainEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

@SuppressWarnings("PMD")
public abstract class BaseService<S extends JpaRepository<T, Long>, T extends AbstractLongDomainEntity> implements BaseServiceInterface<T> {

    /***
     *
     * @return
     */
    public Collection<T> getAll() {
        return getRepository().findAll();
    }


    public Page<T> getPage(PageRequest pageable, T filterDomain) {
        return  getRepository().findAll(Example.of(filterDomain), pageable);
    }

    public Page<T> getPage(PageRequest pageable) {
        return  getRepository().findAll(pageable);
    }


    public Optional<T> findById(Long id) {
        return getRepository().findById(id);
    }


    public T update(Long id, T item) {
        Optional<T> domain = findById(id);
        if (!domain.isPresent()) {
            throw new ItemNotFoundBusinessException("Item not found");
        }
        return getRepository().save(item);
    }

    public void delete(Long id) {
        getRepository().deleteById(id);
    }


    public T create(T item) {
        return getRepository().save(item);
    }

    public abstract S getRepository();
}

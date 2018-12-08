package org.survey.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.collections.IteratorUtils;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

public class CrudRepositoryStub<T, ID extends Serializable> implements PagingAndSortingRepository<T, ID> {
    protected Set<T> entities = new HashSet<>();
    protected Long generatedId = Long.valueOf(1);

    @Override
    public <S extends T> S save(S entity) {
        if (getId(entity) == null) {
            generateId(entity);
        } else {
            delete(entity);
        }
        entities.add(entity);
        return entity;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        List<S> savedEntities = new ArrayList<>();
        for (S entity : entities) {
            savedEntities.add(save(entity));
        }
        return savedEntities;
    }

    @Override
    public Optional<T> findById(ID id) {
        for (T entity : entities) {
            if (id.equals(getId(entity))) {
                return Optional.of(entity);
            }
        }
        return Optional.empty();
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        Set<T> foundEntities = new HashSet<>();
        for (ID id : ids) {
            Optional<T> entity = findById(id);
            if (entity.isPresent()) {
                foundEntities.add(entity.get());
            }
        }
        return foundEntities;
    }

    @Override
    public boolean existsById(ID id) {
        // JPA throws an exception when existById is call with null
        if (id == null) {
            throw new InvalidDataAccessApiUsageException("The given id must not be null");
        }
        return findById(id).isPresent();
    }

    @Override
    public Iterable<T> findAll() {
        return entities;
    }

    @Override
    public long count() {
        return entities.size();
    }

    @Override
    public void deleteById(ID id) {
        Optional<T> entity = findById(id);
        if (entity.isPresent()) {
            entities.remove(entity);
        }
    }

    @Override
    public void delete(T entity) {
        if (existsById(getId(entity))) {
            entities.remove(entity);
        }
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        for (T entity : entities) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        entities.clear();
    }

    @SuppressWarnings("unchecked")
    protected ID getId(T entity) {
        return (ID) BeanHelper.getId(entity);
    }

    @SuppressWarnings("unchecked")
    protected void generateId(T entity) {
        BeanHelper.setGeneratedValue(entity, (ID) generatedId);
        generatedId++;
    }

    @Override
    public Iterable<T> findAll(Sort sort) {
        return findAll();
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return new PageImpl<>(IteratorUtils.toList(findAll().iterator()));
    }
}

package org.survey.repository;

import java.io.Serializable;
import java.util.HashSet;
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

    @SuppressWarnings("unchecked")
    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        Iterable<S> entityList = IteratorUtils.toList(entities.iterator());
        for (T entity : entityList) {
            if (getId(entity) == null) {
                generateId(entity);
            }
            this.entities.add(entity);
        }
        return entityList;
    }

    @Override
    public Optional<T> findById(ID id) {
        if (entities.isEmpty()) {
            return null;
        }
        if (id == null) {
            return null;
        }
        for (T entity : entities) {
            if (id.equals(getId(entity))) {
                return Optional.of(entity);
            }
        }
        return null;
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        Set<T> foundEntities = new HashSet<>();
        for (ID id : ids) {
            foundEntities.add(findById(id).get());
        }
        return foundEntities;
    }

    @Override
    public boolean existsById(ID id) {
        if (id == null) {
            throw new InvalidDataAccessApiUsageException("The given id must not be null");
        }
        return findById(id) != null;
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
//        entities.remove(findById(id));
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
        this.entities.removeAll(IteratorUtils.toList(entities.iterator()));
    }

    @Override
    public void deleteAll() {
        entities.clear();
    }

    @SuppressWarnings("unchecked")
    ID getId(T entity) {
        return (ID) BeanHelper.getId(entity);
    }

    @SuppressWarnings("unchecked")
    void generateId(T entity) {
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

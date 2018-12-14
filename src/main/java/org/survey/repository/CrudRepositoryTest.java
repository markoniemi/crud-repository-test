package org.survey.repository;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.collections.IteratorUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;

@SuppressWarnings("squid:S3655")
public abstract class CrudRepositoryTest<T, ID extends Serializable> {
    protected static final int ENTITY_COUNT = 5;
    protected List<T> orginalEntities = new ArrayList<>();
    protected List<T> savedEntities = new ArrayList<>();
    protected PagingAndSortingRepository<T, ID> entityRepository;
    protected EntityFactory<T, ID> entityFactory;
    protected Comparator<T> entityComparator;

    public abstract PagingAndSortingRepository<T, ID> getEntityRepository();

    @After
    public void tearDown() {
        getEntityRepository().deleteAll();
    }

    @Test
    public void save() {
        orginalEntities = entityFactory.getEntities(ENTITY_COUNT);
        for (int i = 0; i < ENTITY_COUNT; i++) {
            T originalEntity = orginalEntities.get(i);
            T savedEntity = getEntityRepository().save(originalEntity);
            savedEntities.add(savedEntity);
            assertEntity(originalEntity, savedEntity);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    public void saveAll() {
        orginalEntities = entityFactory.getEntities(ENTITY_COUNT);
        List<T> entitiesToSave = new ArrayList<>();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            T originalEntity = orginalEntities.get(i);
            entitiesToSave.add(originalEntity);
        }
        List<T> savedEntities = IteratorUtils.toList(getEntityRepository().saveAll(entitiesToSave).iterator());
        Assert.assertEquals(ENTITY_COUNT, savedEntities.size());
        for (int i = 0; i < ENTITY_COUNT; i++) {
            assertEntity(orginalEntities.get(i), savedEntities.get(i));
            Optional<T> foundEntity = getEntityRepository().findById((ID) BeanHelper.getId(savedEntities.get(i)));
            Assert.assertTrue(foundEntity.isPresent());
            assertEntity(orginalEntities.get(i), foundEntity.get());
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void update() {
        save();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            Optional<T> foundEntity = getEntityRepository().findById((ID) BeanHelper.getId(savedEntities.get(i)));
            Assert.assertTrue(foundEntity.isPresent());
            T updatedEntity = entityFactory.getUpdatedEntity(foundEntity.get());
            BeanHelper.setGeneratedValue(updatedEntity, (ID) BeanHelper.getId(foundEntity.get()));
            getEntityRepository().save(updatedEntity);
            foundEntity = getEntityRepository().findById((ID) BeanHelper.getId(savedEntities.get(i)));
            Assert.assertTrue(foundEntity.isPresent());
            assertEntity(updatedEntity, foundEntity.get());
        }
    }

    @Test
    public void findAll() {
        save();
        @SuppressWarnings("unchecked")
        List<T> entities = IteratorUtils.toList(getEntityRepository().findAll().iterator());
        Assert.assertEquals(ENTITY_COUNT, entities.size());
    }

    @Test
    public void findAllById() {
        save();
        List<ID> ids = new ArrayList<>();
        for (T entity : savedEntities) {
            ids.add((ID) BeanHelper.getId(entity));
        }
        @SuppressWarnings("unchecked")
        List<T> entities = IteratorUtils.toList(getEntityRepository().findAllById(ids).iterator());
        Assert.assertEquals(ENTITY_COUNT, entities.size());
    }

    @Test
    public void findAllByIdWithNonExistentId() {
        save();
        List<ID> ids = new ArrayList<>();
        ids.add(null);
        List<T> entities = IteratorUtils.toList(getEntityRepository().findAllById(ids).iterator());
        Assert.assertEquals(0, entities.size());
    }

    @Ignore
    @Test
    public void findAllWithSort() {
        save();
        // how to get property names
        Sort sort = new Sort("property");
        @SuppressWarnings("unchecked")
        List<T> entities = IteratorUtils.toList(getEntityRepository().findAll(sort).iterator());
        Assert.assertEquals(ENTITY_COUNT, entities.size());
    }

    @Test
    public void findAllWithPageable() {
        save();
        PageRequest pageRequest = new PageRequest(0, ENTITY_COUNT);
        @SuppressWarnings("unchecked")
        List<T> entities = IteratorUtils.toList(getEntityRepository().findAll(pageRequest).iterator());
        Assert.assertEquals(ENTITY_COUNT, entities.size());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void findById() {
        save();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            T originalEntity = orginalEntities.get(i);
            Optional<T> foundEntity = getEntityRepository().findById((ID) BeanHelper.getId(originalEntity));
            Assert.assertTrue(foundEntity.isPresent());
            assertEntity(orginalEntities.get(i), foundEntity.get());
        }
    }

    @Ignore
    @Test
    public void findByIdWithNonExistentId() {
        save();
        Optional<T> foundEntity = getEntityRepository().findById(null);
        Assert.assertFalse(foundEntity.isPresent());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void existsById() {
        save();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            T entity = orginalEntities.get(i);
            getEntityRepository().existsById((ID) BeanHelper.getId(entity));
        }
        // TODO how to test if exists fails?
        // Assert.assertFalse(entityRepository.exists((ID) new Object()));
    }

    @Test(expected = InvalidDataAccessApiUsageException.class)
    public void existsByIdWithNull() {
        Assert.assertFalse(getEntityRepository().existsById(null));
    }

    @Test
    public void count() {
        save();
        Assert.assertEquals(ENTITY_COUNT, getEntityRepository().count());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void deleteById() {
        save();
        for (int i = 0; i < ENTITY_COUNT; i++) {
            T entity = savedEntities.get(i);
            getEntityRepository().deleteById((ID) BeanHelper.getId(entity));
            Assert.assertFalse(getEntityRepository().existsById((ID) BeanHelper.getId(entity)));
        }
        Assert.assertEquals(0, getEntityRepository().count());
    }

    @Test
    public void deleteAll() {
        save();
        getEntityRepository().deleteAll();
        @SuppressWarnings("unchecked")
        List<T> entities = IteratorUtils.toList(getEntityRepository().findAll().iterator());
        Assert.assertEquals(0, entities.size());
    }

    @Test
    public void deleteAll2() {
        save();
        getEntityRepository().deleteAll(savedEntities);
        List<T> entities = IteratorUtils.toList(getEntityRepository().findAll().iterator());
        Assert.assertEquals(0, entities.size());
    }

    public void assertEntity(T originalEntity, T entity) {
        Assert.assertEquals("originalEntity: " + originalEntity + " entity: " + entity, 0,
                entityComparator.compare(originalEntity, entity));
    }
}

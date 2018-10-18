package org.survey.repository;

import org.junit.Assert;
import org.junit.Test;
import org.survey.entity.TestEntity;
import org.survey.entity.TestEntityWithGeneratedId;

public class CrudRepositoryStubTest {

    @Test
    public void getId() {
        CrudRepositoryStub<TestEntity, String> userRepositoryStub = new CrudRepositoryStub<TestEntity, String>();
        TestEntity testEntity = new TestEntity(1L,"username1");
        Assert.assertEquals(1L, userRepositoryStub.getId(testEntity));
        testEntity = new TestEntity(2L,"username2");
        Assert.assertEquals(2L, userRepositoryStub.getId(testEntity));
    }

    @Test
    public void generateId() {
        CrudRepositoryStub<TestEntityWithGeneratedId, Long> testClassRepositoryStub = new CrudRepositoryStub<TestEntityWithGeneratedId, Long>();
        TestEntityWithGeneratedId testClass = new TestEntityWithGeneratedId();
        testClassRepositoryStub.generateId(testClass);
        Assert.assertEquals(Long.valueOf(1), testClassRepositoryStub.getId(testClass));
    }

}

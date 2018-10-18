package org.survey.repository;

import java.lang.reflect.Method;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.junit.Assert;
import org.junit.Test;
import org.survey.entity.EntityWithAnnotationInPrivateMethod;
import org.survey.entity.EntityWithAnnotationInWrongMethod;
import org.survey.entity.TestEntityWithAnnotatedField;
import org.survey.entity.TestEntityWithAnnotatedGetter;
import org.survey.entity.TestEntityWithGeneratedId;
import org.survey.entity.TestEntityWithNoAnnotation;

public class BeanHelperTest {

    @Test
    public void getIdFromAnnotatedField() {
        TestEntityWithAnnotatedField testEntity = new TestEntityWithAnnotatedField();
        testEntity.setUsername("username1");
        Assert.assertEquals("username1", BeanHelper.getId(testEntity));
    }

    @Test
    public void getIdWithAnnotatedGetter() {
        TestEntityWithAnnotatedGetter testEntity = new TestEntityWithAnnotatedGetter();
        testEntity.setUsername("username1");
        Assert.assertEquals("username1", BeanHelper.getId(testEntity));
    }

    @Test
    public void setGeneratedValueWithAnnotatedSetter() {
        TestEntityWithAnnotatedGetter testEntity = new TestEntityWithAnnotatedGetter();
        BeanHelper.setGeneratedValue(testEntity, "username1");
        Assert.assertEquals("username1", testEntity.getUsername());
    }

    @Test
    public void setGeneratedValueToAnnotatedField() {
        TestEntityWithAnnotatedField testEntity = new TestEntityWithAnnotatedField();
        BeanHelper.setGeneratedValue(testEntity, "username1");
        Assert.assertEquals("username1", testEntity.getUsername());
    }

    @Test
    public void getIdFromEntityWithNoAnnotation() {
        TestEntityWithNoAnnotation testEntity = new TestEntityWithNoAnnotation();
        Assert.assertNull(BeanHelper.getId(testEntity));
    }
    @Test
    public void setGeneratedValueToEntityWithNoAnnotation() {
        TestEntityWithNoAnnotation testEntity = new TestEntityWithNoAnnotation();
        BeanHelper.setGeneratedValue(testEntity, "value");
    }
    @Test
    public void getIdFromEntityWithAnnotationInWrongMethod() {
        TestEntityWithNoAnnotation testEntity = new EntityWithAnnotationInWrongMethod();
        Assert.assertNull(BeanHelper.getId(testEntity));
    }
    @Test
    public void setGeneratedValueToEntityWithAnnotationInWrongMethod() {
        EntityWithAnnotationInWrongMethod testEntity = new EntityWithAnnotationInWrongMethod();
        BeanHelper.setGeneratedValue(testEntity, "value");
    }
}

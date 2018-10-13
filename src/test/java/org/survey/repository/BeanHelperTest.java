package org.survey.repository;

import java.lang.reflect.Method;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.junit.Assert;
import org.junit.Test;
import org.survey.entity.TestEntityWithAnnotatedField;
import org.survey.entity.TestEntityWithAnnotatedGetter;
import org.survey.entity.TestEntityWithGeneratedId;

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
        BeanHelper.setGeneratedValue(testEntity,  "username1");
        Assert.assertEquals("username1", testEntity.getUsername());
    }
    @Test
    public void setGeneratedValueToAnnotatedField() {
        TestEntityWithAnnotatedField testEntity = new TestEntityWithAnnotatedField();
        BeanHelper.setGeneratedValue(testEntity,  "username1");
        Assert.assertEquals("username1", testEntity.getUsername());
    }
//
//    @Test
//    public void getValueOfAnnotatedFieldWithAnnotatedGetter() {
//        TestEntityWithAnnotatedGetter testEntity = new TestEntityWithAnnotatedGetter();
//        testEntity.setUsername("username1");
//        Assert.assertEquals("username1", BeanHelper.getValueOfAnnotatedField(testEntity, Id.class));
//
//    }
//
//    @Test
//    public void setValueOfAnnotatedFieldWithAnnotatedGetter() {
//        TestEntityWithAnnotatedGetter testEntity = new TestEntityWithAnnotatedGetter();
//        BeanHelper.setValueOfAnnotatedField(testEntity, Id.class, "username1");
//        Assert.assertEquals("username1", testEntity.getUsername());
//    }
//
//    @Test
//    public void setValueOfAnnotatedFieldWithGeneratedValueAnnotation() {
//        TestEntityWithGeneratedId testClass = new TestEntityWithGeneratedId();
//        BeanHelper.setValueOfAnnotatedField(testClass, GeneratedValue.class, Long.valueOf(1));
//        Assert.assertEquals(Long.valueOf(1), testClass.getId());
//    }
//    @Test
//    public void setValueOfAnnotatedGetter() {
//        TestEntityWithAnnotatedGetter testClass = new TestEntityWithAnnotatedGetter();
//        BeanHelper.setValueOfAnnotatedField(testClass, Id.class, "test");
//        Assert.assertEquals("test", testClass.getUsername());
//    }
}

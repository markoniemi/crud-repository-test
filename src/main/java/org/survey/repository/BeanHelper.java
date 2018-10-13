package org.survey.repository;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

/**
 * Utility class for getting and setting values of annotated field of a bean.
 */
public class BeanHelper {
    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";

    /**
     * Utility class should not have a public constructor.
     */
    private BeanHelper() {
    }

    /**
     * Return a value of a field from object which is a field marked with @Id
     * annotation.
     * 
     * @param object
     *            Object from which to read the value.
     */
    public static Object getId(Object object) {
        return getValueOfAnnotatedField(object, Id.class);
    }
    /**
     * Return a value of a field from object which is a field marked with @Id
     * annotation.
     * 
     * @param object
     *            Object from which to read the value.
     */
    public static void setGeneratedValue(Object object, Object value) {
        setValueOfAnnotatedField(object, GeneratedValue.class,value);
    }

    /**
     * Return a value of a field from object, which is marked with a given
     * annotation. Annotation can mark either a field or a getter method. Used
     * for reading an Id of an Entity.
     * 
     * @param object
     *            Object from which to read the value.
     * @param annotation
     *            Annotation which marks the field or getter.
     */
    private static Object getValueOfAnnotatedField(Object object, Class<? extends Annotation> annotation) {
        try {
            Field[] fields = FieldUtils.getFieldsWithAnnotation(object.getClass(), annotation);
            for (Field field : fields) {
                return PropertyUtils.getProperty(object, field.getName());
            }
            Method[] methods = MethodUtils.getMethodsWithAnnotation(object.getClass(), annotation);
            {
                for (Method method : methods) {
                    if (isGetter(method)) {
                        return method.invoke(object);
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
    
    

    /**
     * Set the value of a field from object, which is marked with a given
     * annotation. Annotation can mark either a field or a setter method. Used
     * for setting an Id of an Entity.
     * 
     * @param object
     *            Object to which to read the value.
     * @param annotation
     *            Annotation which marks the field or setter.
     * @param value
     *            Value to set.
     */
    private static void setValueOfAnnotatedField(Object object, Class<? extends Annotation> annotation, Object value) {
        try {
            Field[] fields = FieldUtils.getFieldsWithAnnotation(object.getClass(), annotation);
            for (Field field : fields) {
                PropertyUtils.setProperty(object, field.getName(), value);
            }
            Method[] methods = MethodUtils.getMethodsWithAnnotation(object.getClass(), annotation);
            {
                for (Method method : methods) {
                    if (isSetter(method)) {
                        method.invoke(object, value);
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    private static boolean isGetter(Method method) {
        return method.getName().startsWith(GETTER_PREFIX);
    }

    private static boolean isSetter(Method method) {
        return method.getName().startsWith(SETTER_PREFIX);
    }
}

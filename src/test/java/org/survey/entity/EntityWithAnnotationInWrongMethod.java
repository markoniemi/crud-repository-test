package org.survey.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class EntityWithAnnotationInWrongMethod extends TestEntityWithNoAnnotation {

    @Id
    @GeneratedValue
    public void wrongMethod() {

    }
}

package org.survey.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class TestEntityWithAnnotatedField {
    @Id
    @GeneratedValue
    private String username;
}
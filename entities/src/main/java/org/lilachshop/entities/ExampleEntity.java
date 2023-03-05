package org.lilachshop.entities;


import javax.persistence.*;

import org.lilachshop.entities.ExampleEnum;

import java.io.Serializable;


@Entity
@Table(name = "ExampleEntities")
public class ExampleEntity implements Serializable {
    protected ExampleEntity() {
    }

    public ExampleEntity(ExampleEnum exampleEnum) {
        this.exampleEnum = exampleEnum;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "entity_id")
    int id;

    @Enumerated
    @Column(name = "enum", nullable = false)
    ExampleEnum exampleEnum;

    public int getId() {
        return id;
    }

    public ExampleEnum getExampleEnum() {
        return exampleEnum;
    }

    public void setExampleEnum(ExampleEnum exampleEnum) {
        this.exampleEnum = exampleEnum;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Enum: " + exampleEnum;
    }
}

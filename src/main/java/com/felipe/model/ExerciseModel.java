package com.felipe.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "exercises")
public class ExerciseModel extends PanacheEntity {

    @NotBlank(message = "O nome do exercício é obrigatório!")
    @Size(max = 100)
    public String name;

    @Size(max = 50)
    public String grupoMuscular;

    public ExerciseModel(String name, String grupoMuscular) {
        this.name = name;
        this.grupoMuscular = grupoMuscular;
    }

    public ExerciseModel() {
    }
}

package com.felipe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "workout_plans")
//FICHA DE TREINO EFETIVAMENTE, COM VÁRIOS EXERCÍCIOS E REPS
public class WorkoutPlanModel extends PanacheEntity {

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_email")

    public UserModel user;

    public LocalDate startDate;

    public String notas;

    @OneToMany(mappedBy = "workoutPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    public List<WorkoutExerciseModel> exercises;

    public WorkoutPlanModel(UserModel user, LocalDate startDate, String notas, List<WorkoutExerciseModel> exercises) {
        this.user = user;
        this.startDate = startDate;
        this.notas = notas;
        this.exercises = exercises;
    }

    public WorkoutPlanModel() {
    }
}

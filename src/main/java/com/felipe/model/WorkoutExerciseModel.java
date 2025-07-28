package com.felipe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "workout_exercises")
//Associa um exercício com suas reps, e terei vários desse no WorkoutPlan
public class WorkoutExerciseModel extends PanacheEntity {

    @ManyToOne
    @JoinColumn(name = "workout_plan_id", nullable = false)
    @JsonIgnore
    public WorkoutPlanModel workoutPlan;

    @ManyToOne
    @JoinColumn(name = "exercise_id", nullable = false)
    public ExerciseModel exercise;

    @Min(value = 1, message = "Pelo menos uma série!")
    public int sets;

    @Min(value = 1, message = "Pelo menos uma repetição!")
    public int reps;

    public String notes;

    public WorkoutExerciseModel() {
    }

    public WorkoutExerciseModel(WorkoutPlanModel workoutPlan, ExerciseModel exercise, int sets, int reps, String notes) {
        this.workoutPlan = workoutPlan;
        this.exercise = exercise;
        this.sets = sets;
        this.reps = reps;
        this.notes = notes;
    }
}

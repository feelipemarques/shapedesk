package com.felipe.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class WorkoutExerciseRequest {

    @NotNull(message = "ID do exercício é obrigatório!")
    public Long exerciseId;

    @Size(min = 1)
    public int sets;

    @Size(min = 1)
    public int reps;

    public String notes;
}

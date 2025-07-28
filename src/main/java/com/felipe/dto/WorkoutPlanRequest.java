package com.felipe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class WorkoutPlanRequest {

    @NotBlank(message = "Email obrigatório!")
    public String userEmail;

    public LocalDate startDate;

    public String notas;

    public List<WorkoutExerciseRequest> exercises;
}

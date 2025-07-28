package com.felipe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ExerciseRequest {

    @NotBlank(message = "O nome do exercício é obrigatório!")
    @Size(max = 100)
    public String nome;

    @Size(max = 50)
    public String grupoMuscular;

}

package com.felipe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.felipe.enums.Gender;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "profiles")
public class UserProfileModel extends PanacheEntityBase {

    @Id
    public String email;

    @OneToOne
    @MapsId
    @JoinColumn(name = "email")
    @JsonIgnore
    public UserModel user;

    @NotBlank
    public String nome;

    @NotNull
    public LocalDate dataNascimento;

    @NotNull
    @Min(value = 10)
    public Double pesoKg;

    @NotNull
    @Min(value = 100)
    public Double alturaCm;

    @Enumerated(EnumType.STRING)
    public Gender genero;

    public UserProfileModel(UserModel user) {
        this.user = user;
        this.email = user.email; // Garante que o ID do perfil seja o email do usuário
    }

    public UserProfileModel(){}

    @JsonProperty
    public int getIdade(){
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    @JsonProperty
    public double getImc(){
        double alturaMetros = alturaCm / 100;
        return pesoKg / (alturaMetros * alturaMetros);
    }

    @JsonProperty
    public double getTmb(){
        if(genero == Gender.MASCULINO){
            return (10 * pesoKg) + (6.25 * alturaCm) - (5 * getIdade()) + 5;
        }else{
            return (10 * pesoKg) + (6.25 * alturaCm) - (5 * getIdade()) - 161;
        }
    }
}

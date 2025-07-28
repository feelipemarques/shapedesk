package com.felipe.controller;

import com.felipe.dto.WorkoutExerciseRequest;
import com.felipe.dto.WorkoutPlanRequest;
import com.felipe.model.ExerciseModel;
import com.felipe.model.UserModel;
import com.felipe.model.WorkoutExerciseModel;
import com.felipe.model.WorkoutPlanModel;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;

@Path("/fichas-de-treino")
public class WorkoutPlanController {

    @Context
    SecurityContext securityContext;

    @POST
    @RolesAllowed("ADMIN")
    @Transactional
    public Response createOrUpdateWorkoutPlan(@Valid WorkoutPlanRequest request){
        UserModel user = UserModel.findById(request.userEmail);
        if(user == null){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        WorkoutPlanModel workoutPlan = WorkoutPlanModel.find("user", user).firstResult();
        if(workoutPlan == null){
            workoutPlan = new WorkoutPlanModel();
            workoutPlan.user = user;
        }

        workoutPlan.startDate = LocalDate.now();
        workoutPlan.notas = request.notas;

        if(workoutPlan.exercises != null){
            workoutPlan.exercises.clear();
        }else{
            workoutPlan.exercises = new ArrayList<>();
        }

        for(WorkoutExerciseRequest exerciseRequest : request.exercises){
            ExerciseModel exercise = ExerciseModel.findById(exerciseRequest.exerciseId);
            if(exercise == null){
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            WorkoutExerciseModel workoutExercise = new WorkoutExerciseModel(
                    workoutPlan,
                    exercise,
                    exerciseRequest.sets,
                    exerciseRequest.reps,
                    exerciseRequest.notes
            );
            workoutPlan.exercises.add(workoutExercise);
        }
        workoutPlan.persist();
        return Response.ok(workoutPlan).build();
    }

    @GET
    @Path("/my-plan")
    @Authenticated
    public Response getMyWorkoutPlan() {
        Principal callerPrincipal = securityContext.getUserPrincipal();
        String loggedInUserEmail = callerPrincipal.getName();

        UserModel user = UserModel.findById(loggedInUserEmail);
        if (user == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuário logado não encontrado.").build();
        }

        WorkoutPlanModel workoutPlan = WorkoutPlanModel.find("user", user).firstResult();
        if (workoutPlan == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Você não tem uma ficha de treino atribuída.").build();
        }
        // Para garantir que os exercícios sejam carregados (devido ao FetchType.LAZY)
        workoutPlan.exercises.size(); // Força o carregamento da coleção
        return Response.ok(workoutPlan).build();
    }
}

package com.felipe.controller;

import com.felipe.dto.ExerciseRequest;
import com.felipe.model.ExerciseModel;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

@Path("/exercises")
public class ExerciseController {

    @POST
    @RolesAllowed("ADMIN")
    @Transactional
    public Response createExercise(@Valid ExerciseRequest request){
        ExerciseModel exercise = new ExerciseModel(request.nome, request.grupoMuscular);
        exercise.persist();
        return Response.status(Response.Status.CREATED).entity(exercise).build();
    }

    @GET
    @RolesAllowed({"ADMIN", "USER"})
    public Response getAllExercises(){
        return Response.ok(ExerciseModel.listAll()).build();
    }

}

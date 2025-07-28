package com.felipe.controller;

import com.felipe.model.UserModel;
import com.felipe.model.UserProfileModel;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.security.Principal;

@Path("/profile")
public class UserProfileController {

    @Context
    SecurityContext securityContext;

    @POST
    @Authenticated
    @Transactional
    public Response createProfile(UserProfileModel profileData){
        Principal callerPrincipal = securityContext.getUserPrincipal();
        String loggedUserEmail = callerPrincipal.getName();

        if(!loggedUserEmail.equals(profileData.email)){
            return Response
                    .status(Response.Status.UNAUTHORIZED)
                    .entity("Você só pode gerenciar o próprio perfil")
                    .build();
        }

        UserModel user = UserModel.findById(loggedUserEmail);
        if(user == null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Usuário não encontrado.")
                    .build();
        }

        UserProfileModel existingProfile = UserProfileModel.findById(loggedUserEmail);
        if(existingProfile == null){
            //Cria novo perfil se não existir
            UserProfileModel newProfile = new UserProfileModel(user);
            newProfile.nome = profileData.nome;
            newProfile.dataNascimento = profileData.dataNascimento;
            newProfile.alturaCm = profileData.alturaCm;
            newProfile.pesoKg = profileData.pesoKg;
            newProfile.genero = profileData.genero;
            user.profile = newProfile;
            user.persist();
            return Response.status(Response.Status.CREATED).entity(newProfile).build();
        } else{
            existingProfile.nome = profileData.nome;
            existingProfile.pesoKg = profileData.pesoKg;
            existingProfile.alturaCm = profileData.alturaCm;
            existingProfile.genero = profileData.genero;
            existingProfile.persist();
            return Response.ok(existingProfile).build();
        }
    }

    @GET
    @Authenticated
    public Response getMyProfile(){
        Principal callerPrincipal = securityContext.getUserPrincipal();
        String loggedUserEmail = callerPrincipal.getName();

        UserProfileModel profile = UserProfileModel.findById(loggedUserEmail);
        if(profile == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Usuário não existe!").build();
        }
        return Response.ok(profile).build();
    }

    @Path("/{email}")
    @GET
    @RolesAllowed("ADMIN")
    public Response getOtherProfile(@PathParam("email") String email){
        UserProfileModel profile = UserProfileModel.findById(email);
        if(profile == null){
            return Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado!").build();
        }
        return Response.ok(profile).build();
    }

}

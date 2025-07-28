package com.felipe.controller;

import com.felipe.dto.UpdateRolesRequest;
import com.felipe.model.UserModel;
import io.smallrye.jwt.build.Jwt;
import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/auth")
public class AuthController {

    public static class LoginRequest{
        public String email;
        public String password;
    }

    @POST
    @Path("/register")
    @Transactional
    public Response register(@Valid UserModel newUser){
        if(UserModel.findById(newUser.email) != null){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Usuário já cadastrado!")
                    .build();
        }
        newUser.password = newUser.hashPassword(newUser.password);

        if(Objects.equals(newUser.email, "feelipemarquees@gmail.com")){
            newUser.roles = "ADMIN";
        }else{
            newUser.roles = "USER";
        }
        newUser.persist();

        return Response.status(Response.Status.CREATED)
                .entity(newUser)
                .build();
    }

    @POST
    @Path("/login")
    public Response login(LoginRequest loginRequest){
        UserModel user = UserModel.findById(loginRequest.email);
        if(user != null && user.checkPassword(loginRequest.password)){
            Set<String> roles = new HashSet<>();
            if(user.roles != null && !user.roles.isEmpty()){
                for (String role : user.roles.split(",")){
                    roles.add(role.trim());
                }
            }

            String token = Jwt
                    .issuer("shapedesk")
                    .upn(user.email)
                    .groups(user.roles)
                    .expiresIn(3600)
                    .sign();

            Map<String, String> response = Map.of("access-token", token, "expires-in", "3600");

            return Response.ok(response).build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("Usuário ou senha inválidos!").build();
    }

    @POST
    @Path("/admin/roles")
    @RolesAllowed("ADMIN")
    @Transactional
    public Response updateRoles(@Valid UpdateRolesRequest rolesRequest){
        UserModel targetUser = UserModel.findById(rolesRequest.email);

        if(targetUser == null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Usuário não cadastrado!")
                    .build();
        }
        String newRolesString = rolesRequest.newRoles
                .stream()
                .map(String::trim)
                .collect(Collectors.joining(","));
        targetUser.roles = newRolesString;
        targetUser.persist();

        return Response.ok().entity("Roles atualizadas para: " + rolesRequest.newRoles).build();
    }

}

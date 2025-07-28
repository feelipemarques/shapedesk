package com.felipe.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "users")
public class UserModel extends PanacheEntityBase {

    @Id
    @Email
    @NotBlank
    public String email;

    @Size(min = 6, message = "Senha precisa de 6 caracteres!")
    @NotBlank
    public String password;

    public String roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public UserProfileModel profile;

    public String hashPassword(String plainPassword){
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public boolean checkPassword(String plainPassword){
        return BCrypt.checkpw(plainPassword, this.password);
    }

}

package com.br.sgs.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    public interface UserView {
        public static interface RegistrationPost {}
        public static interface UserPut {}
        public static interface PasswordPut {}
        public static interface ImagePut {}
    }
	
    @NotBlank(groups = UserView.RegistrationPost.class)
    @Size(min = 4, max = 50, groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
	private String name;
    
    @NotBlank(groups = UserView.RegistrationPost.class)
    @Email(groups = UserView.RegistrationPost.class)
    @JsonView(UserView.RegistrationPost.class)
    private String email;
	
    @CPF(groups = {UserView.RegistrationPost.class, UserView.UserPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.UserPut.class})
	private String document;
	
	@NotBlank(groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @Size(min = 6, max = 20, groups = {UserView.RegistrationPost.class, UserView.PasswordPut.class})
    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
	private String password;

    
}

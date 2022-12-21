package com.br.sgs.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;

import com.br.sgs.dtos.UserDto.UserView;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDto {

	
	public interface ClientView {
        public static interface RegistrationPost {}
        public static interface ClientPut {}
    }
	
    @NotBlank(groups = UserView.RegistrationPost.class)
    @Size(min = 4, max = 50, groups = ClientView.RegistrationPost.class)
    @JsonView(ClientView.RegistrationPost.class)
	private String name;
	
    @CPF(groups = {ClientView.RegistrationPost.class, ClientView.ClientPut.class})
    @JsonView({ClientView.RegistrationPost.class, ClientView.ClientPut.class})
	private String document;
    
    @NotBlank(groups = ClientView.RegistrationPost.class)
    @JsonView(ClientView.RegistrationPost.class)
    private String organization;
    
}

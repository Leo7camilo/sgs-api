package com.br.sgs.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.br.sgs.enums.RoleState;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDto {
	
	public interface RoleView {
	    public static interface RegistrationPost {}
	    public static interface RolePut {}
	    public static interface RoleStatusPut {}
	}
	
	@NotBlank(groups = RoleView.RegistrationPost.class)
	@Size(min = 4, max = 50, groups = {RoleView.RegistrationPost.class, RoleView.RolePut.class})
	@JsonView(RoleView.RegistrationPost.class)
	private String description;
	
	@NotBlank(groups = RoleView.RegistrationPost.class)
	@Size(min = 4, max = 50, groups = {RoleView.RegistrationPost.class, RoleView.RolePut.class})
	@JsonView(RoleView.RegistrationPost.class)
	private String value;
	
	@NotBlank(groups = RoleView.RoleStatusPut.class)
	@Size(min = 4, max = 50, groups = RoleView.RoleStatusPut.class)
	@JsonView(RoleView.RoleStatusPut.class)
	private RoleState status;
    
}

package com.br.sgs.dtos;

import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.br.sgs.dtos.RoleDto.RoleView;
import com.br.sgs.enums.QueueState;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QueueDto {
	
	public interface QueueView {
	    public static interface RegistrationPost {}
	    public static interface QueuePut {}
	    public static interface QueueStatusPut {}
	}
	
	@NotBlank(groups = RoleView.RegistrationPost.class)
	@Size(min = 4, max = 50, groups = {RoleView.RegistrationPost.class, RoleView.RolePut.class})
	@JsonView(RoleView.RegistrationPost.class)
	private String description;

	@NotBlank(groups = RoleView.RoleStatusPut.class)
	@Size(min = 4, max = 50, groups = RoleView.RoleStatusPut.class)
	@JsonView(RoleView.RoleStatusPut.class)
	private QueueState status;
	
	@NotBlank(groups = RoleView.RegistrationPost.class)
	@JsonView(RoleView.RegistrationPost.class)
	private Set<UUID> idRoles;

}

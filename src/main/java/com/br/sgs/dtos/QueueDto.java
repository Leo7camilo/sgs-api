package com.br.sgs.dtos;

import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	
	@NotBlank(groups = {QueueView.RegistrationPost.class, QueueView.QueuePut.class})
	@Size(min = 4, max = 50, groups = {QueueView.RegistrationPost.class, QueueView.QueuePut.class})
	@JsonView({QueueView.RegistrationPost.class, QueueView.QueuePut.class})
	private String description;

	@NotBlank(groups = QueueView.QueuePut.class)
	@Size(min = 4, max = 50, groups = QueueView.QueuePut.class)
	@JsonView(QueueView.QueuePut.class)
	private QueueState status;
	
	@NotNull(groups = QueueView.RegistrationPost.class)
	@JsonView(QueueView.RegistrationPost.class)
	private Set<UUID> idRoles;
	
	@NotNull(groups = {QueueView.RegistrationPost.class, QueueView.QueuePut.class})
	@JsonView({QueueView.RegistrationPost.class, QueueView.QueuePut.class})
	private Integer priority;

}

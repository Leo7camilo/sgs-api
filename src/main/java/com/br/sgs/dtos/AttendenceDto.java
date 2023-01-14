package com.br.sgs.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendenceDto {
	
	public interface AttendenceView {
	    public static interface RegistrationPost {}
	    public static interface Calls {}
	}
	
	@NotNull(groups = AttendenceView.RegistrationPost.class)
	@JsonView(AttendenceView.RegistrationPost.class)
	private List<UUID> idQueueList = new ArrayList<>();
	
	@NotNull(groups = AttendenceView.RegistrationPost.class)
	@JsonView(AttendenceView.RegistrationPost.class)
	private Integer passaword;
	
	@NotNull(groups = AttendenceView.Calls.class)
	@JsonView(AttendenceView.Calls.class)
	private UUID idUser;
	
	
	//POSTO DE TRABALHO
}

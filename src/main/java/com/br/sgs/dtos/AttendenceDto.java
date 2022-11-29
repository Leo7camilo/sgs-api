package com.br.sgs.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AttendenceDto {
	
	public interface TerminalView {
	    public static interface RegistrationPost {}
	}
	
	@NotBlank(groups = TerminalView.RegistrationPost.class)
	@JsonView(TerminalView.RegistrationPost.class)
	private List<UUID> idQueueList = new ArrayList<>();
 
}

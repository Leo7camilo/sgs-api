package com.br.sgs.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.br.sgs.enums.TerminalState;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerminalDto {
	
	public interface TerminalView {
	    public static interface RegistrationPost {}
	    public static interface TerminalPut {}
	    public static interface TerminalStatusPut {}
	}
	
	@NotBlank(groups = {TerminalView.RegistrationPost.class, TerminalView.TerminalPut.class})
	@Size(min = 4, max = 50, groups = {TerminalView.RegistrationPost.class, TerminalView.TerminalPut.class})
	@JsonView({TerminalView.RegistrationPost.class, TerminalView.TerminalPut.class})
	private String name;
	
	@NotBlank(groups = TerminalView.TerminalStatusPut.class)
	@Size(min = 4, max = 50, groups = TerminalView.TerminalStatusPut.class)
	@JsonView(TerminalView.TerminalStatusPut.class)
	private TerminalState status;
 
}

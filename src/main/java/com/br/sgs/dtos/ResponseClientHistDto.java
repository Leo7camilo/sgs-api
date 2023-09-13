package com.br.sgs.dtos;

import java.util.List;

import lombok.Data;

@Data
public class ResponseClientHistDto {
	
	private ClientHistDto parenteAttendence;
	private List<ClientHistDto> childAttendence;
	
}

package com.br.sgs.dtos;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EngagementClientSumarizedDto {

	private String status;
	private BigInteger count;
	
}

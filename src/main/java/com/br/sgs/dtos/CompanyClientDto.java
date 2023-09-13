package com.br.sgs.dtos;

import java.math.BigInteger;

import lombok.Data;

@Data
public class CompanyClientDto {
	
	private String company;
	
	private BigInteger count;
	
	private String description;
	
}

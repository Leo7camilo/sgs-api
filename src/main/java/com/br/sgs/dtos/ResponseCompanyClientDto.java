package com.br.sgs.dtos;

import java.math.BigInteger;
import java.util.List;

import lombok.Data;

@Data
public class ResponseCompanyClientDto {
	
	private List<CompanyClientDto> companyClientDto;
	
	private BigInteger totalAttendence;
	
}

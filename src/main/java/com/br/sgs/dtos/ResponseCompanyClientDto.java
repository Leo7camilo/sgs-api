package com.br.sgs.dtos;

import java.util.List;

import lombok.Data;

@Data
public class ResponseCompanyClientDto {
	
	private List<CompanyClientDto> companyClientDto;
	
	private Long totalAttendence;
	
}

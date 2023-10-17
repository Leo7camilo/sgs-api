package com.br.sgs.dtos;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EngagementClientDto {

	private List<EngagementClientSumarizedDto> listEngagementClientSumarized = new ArrayList<>();
	
	private List<EngagementClientDetailedDto> listEngagementClientDetailed = new ArrayList<>();;

	
}

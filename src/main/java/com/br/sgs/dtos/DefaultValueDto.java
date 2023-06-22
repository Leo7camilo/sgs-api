package com.br.sgs.dtos;

import java.io.Serializable;

import lombok.Data;

@Data
public class DefaultValueDto implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = -9114891688251603978L;
	
	private String description;
	private String value;
	
}

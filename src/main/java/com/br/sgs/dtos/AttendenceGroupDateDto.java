package com.br.sgs.dtos;

import java.math.BigInteger;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class AttendenceGroupDateDto {
	
	private String description;
	
	private BigInteger count;	
	
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime hour;

}

package com.br.sgs.model;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity(name = "COMPANY")
public class Company {
	
	
	@Id
	private UUID uuid;
	
	@NotNull
	private String name;
	
	@NotNull
	private String document;
	
	private LocalDateTime dtCreated;
	
}

package com.br.sgs.model;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "TERMINAL")
public class Terminal {
	
	@Id
	private UUID uuid;
	
	@NotNull
	private String name;
	
	@JsonIgnoreProperties
	private UUID uuidCompany;
	
}

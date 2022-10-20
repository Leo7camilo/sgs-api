package com.br.sgs.models;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "TERMINAL")
public class Terminal {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID uuid;
	
	@NotNull
	private String name;
	
	@JsonIgnoreProperties
	private UUID uuidCompany;
	
}

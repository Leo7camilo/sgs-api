package com.br.sgs.models;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.Data;


@Data
@Entity
@Table(name = "COMPANY")
public class Company {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID uuid;
	
	@NotNull
	private String name;
	
	@NotNull
	private String document;
	
	private LocalDateTime dtCreated;
	
}

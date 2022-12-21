package com.br.sgs.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.br.CPF;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "CLIENT")
public class ClientModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type="uuid-char")
	private UUID idClient;
	
	@NotNull
	@Column(nullable = false, length = 150)
	private String name;
	
	@CPF
	@Column(nullable = false, unique = true, length = 11)
	private String document;
	
	@Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime dtCreated;
	
	@NotNull
	@Column(nullable = false, length = 150)
	private String organization;

	@OneToOne
	@JoinColumn(name = "companyId")
	private CompanyModel company;
	
	
}

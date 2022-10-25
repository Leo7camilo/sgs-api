package com.br.sgs.models;

import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.br.sgs.enums.RoleState;
import com.sun.istack.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "ROLE")
public class RoleModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID roleId;
	
	@NotNull
	@Column(nullable = false, length = 150)
	private String description;
	
	@NotNull
	@Column(nullable = false, length = 150)
	private String value;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
    private RoleState status;
	
	@NotNull
	private UUID idCompany;

}

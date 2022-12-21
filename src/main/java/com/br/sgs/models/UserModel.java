package com.br.sgs.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.br.CPF;

import com.br.sgs.enums.UserStatus;
import com.br.sgs.enums.UserType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import lombok.Data;


@Data
@Entity
@Table(name = "USER")
public class UserModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type="uuid-char")
	private UUID userId;
	
	@NotNull
	@Column(nullable = false, length = 150)
	private String username;
	
	@Column(nullable = false, length = 150)
	private String fullName;
	
	@NotNull
	@CPF
	@Column(length = 20)
	private String document;
	
	@Email
	@Column(nullable = false, unique = true, length = 50)
	private String email;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
    private UserType userType;
	
	@Column(nullable = false, length = 255)
    @JsonIgnore
	private String password;
	
	@Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime dtCreated;
	
	@Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime dtUpdated;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus;
	
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(    name = "TB_USER_ROLES",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleModel> roles = new HashSet<>();
	
	//@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//	@ManyToMany(fetch = FetchType.LAZY)
//	@JoinTable(    name = "TB_USERS_COMPANY",
//          joinColumns = @JoinColumn(name = "user_id"),
//          inverseJoinColumns = @JoinColumn(name = "company_id"))
//	private Set<CompanyModel> companys = new HashSet<>();
	
}

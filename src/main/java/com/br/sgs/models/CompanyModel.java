package com.br.sgs.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.br.CNPJ;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "COMPANY")
public class CompanyModel implements Serializable {
	
	private static final long serialVersionUID = -521297201595944303L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type="uuid-char")
	private UUID companyId;
	
	@NotBlank
	@NotNull
	private String name;
	
	@CNPJ
	@NotBlank
	@NotNull
	private String document;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime dtCreated;
	
//	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(    name = "TB_COMPANY_USERS",
//            joinColumns = @JoinColumn(name = "company_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))
//	@ManyToMany(mappedBy = "companys",fetch = FetchType.LAZY)
//	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//	@EqualsAndHashCode.Exclude
//    private Set<UserModel> users;
	
}

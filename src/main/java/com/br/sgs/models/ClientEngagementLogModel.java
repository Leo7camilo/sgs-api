package com.br.sgs.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.br.sgs.enums.ClientEngagementLogState;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "CLIENT_ENGAGEMENT_LOG")
@NoArgsConstructor
@AllArgsConstructor
public class ClientEngagementLogModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type="uuid-char")
	private UUID clientEngagementLogId;
	
	@ManyToOne
	@JoinColumn(name = "clientId")
	private ClientModel client;
	
	@Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dtCreated;
	
	@NotNull
	@Column(nullable = false, length = 5)
	@Enumerated(EnumType.STRING)
	private ClientEngagementLogState status;
	
	@ManyToOne
	@JoinColumn(name = "companyId")
	private CompanyModel company;
	
}

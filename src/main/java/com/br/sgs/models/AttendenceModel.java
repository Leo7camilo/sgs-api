package com.br.sgs.models;

import java.io.Serializable;
import java.time.LocalDateTime;
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

import com.br.sgs.enums.AttendenceState;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ATTENDENCE")
@NoArgsConstructor
@AllArgsConstructor
public class AttendenceModel implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type="uuid-char")
	private UUID attendenceId;
	
	private Integer password;
	
	////@Type(type="uuid-char")
	//private UUID queueId;
	
	@ManyToOne
	@JoinColumn(name = "queueId")
	private QueueModel queue;
	
	@ManyToOne
	@JoinColumn(name = "clientId")
	private ClientModel client;
	
	@Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime dtCreated;
	
	@Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime dtUpdated;
	
	@NotNull
	@Column(nullable = false, length = 15)
	@Enumerated(EnumType.STRING)
	private AttendenceState status;
	
	@ManyToOne
	@JoinColumn(name = "companyId")
	private CompanyModel company;
	
	@ManyToOne
	@JoinColumn(name = "terminalId")
	private TerminalModel terminal;
	
}

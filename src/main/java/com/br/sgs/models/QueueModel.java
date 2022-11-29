package com.br.sgs.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.br.sgs.enums.QueueState;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "QUEUE")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class QueueModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type="uuid-char")
	private UUID queueId;
	
	@NotNull
	@Column(nullable = false, length = 150)
	private String description;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
    private QueueState status;
	
	@NotNull
	private UUID idCompany;
	
	@NotNull
	private Integer priority;

}

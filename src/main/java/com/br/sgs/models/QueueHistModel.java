package com.br.sgs.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.Data;


@Data
@Entity
@Table(name = "QUEUE_HIST")
public class QueueHistModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID idQueueHist;
	
	@NotNull
	private UUID idQueue;
	
	@NotNull
	@Column(nullable = false, length = 150)
	private String description;
	
	@NotNull
	private UserModel idUser;
	
	@NotNull
	private LocalDateTime tsChange;

}

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@Entity
@Table(name = "QUEUE_HIST")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class QueueHistModel implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type="uuid-char")
	private UUID queueHistId;
	
	@ManyToOne
	@JoinColumn(name = "queueId")
	private QueueModel queue;
	
	@NotNull
	@Column(nullable = false, length = 150)
	private String description;
	
	@ManyToOne
	@JoinColumn(name = "userId")
	private UserModel user;
	
	@NotNull
	@JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime tsChange;

}

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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import com.br.sgs.enums.ProfileState;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "PROFILE")
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProfileModel implements Serializable{

	private static final long serialVersionUID = -7443266171836685376L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Type(type="uuid-char")
	private UUID profileId;
	
	@NotNull
	private String description;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
    private ProfileState status;
	
	@Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
	private LocalDateTime dtChange;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(    name = "TB_PROFILE_QUEUE",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "queue_id"))
    private Set<QueueModel> queues = new HashSet<>();
	
	@ManyToOne
	@JoinColumn(name = "companyId")
	private CompanyModel company;

}

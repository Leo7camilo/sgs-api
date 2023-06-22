package com.br.sgs.models;

import java.io.Serializable;
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

import com.br.sgs.enums.QueueState;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(    name = "TB_QUEUE_ROLES",
            joinColumns = @JoinColumn(name = "queue_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleModel> roles = new HashSet<>();

	@NotNull
	private Integer priority;
	
	@ManyToOne
	@JoinColumn(name = "companyId")
	private CompanyModel company;
	
	
	@JsonIgnore
	public String getQueue() {
		 return this.queueId.toString();
	}
}

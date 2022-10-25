package com.br.sgs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.sgs.models.RoleModel;

public interface RoleRepository extends JpaRepository<RoleModel, UUID>{
	
	boolean existsByDescription(String description);
	
	boolean existsById(UUID id);

}

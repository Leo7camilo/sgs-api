package com.br.sgs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.RoleModel;


@Repository
public interface RoleRepository extends JpaRepository<RoleModel, UUID>{
	
	boolean existsByDescription(String description);
	
	boolean existsById(UUID id);

}

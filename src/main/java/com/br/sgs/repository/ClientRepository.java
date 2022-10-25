package com.br.sgs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.sgs.models.ClientModel;

public interface ClientRepository extends JpaRepository<ClientModel, UUID>{
	
	boolean existsByDocument(String document);

}

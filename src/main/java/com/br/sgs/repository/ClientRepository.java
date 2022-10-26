package com.br.sgs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.ClientModel;

@Repository
public interface ClientRepository extends JpaRepository<ClientModel, UUID>{
	
	boolean existsByDocument(String document);

}

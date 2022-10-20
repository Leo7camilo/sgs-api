package com.br.sgs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.sgs.models.UserModel;

public interface UserRepository extends JpaRepository<UserModel, UUID>{

	boolean existsByEmail(String email);

}

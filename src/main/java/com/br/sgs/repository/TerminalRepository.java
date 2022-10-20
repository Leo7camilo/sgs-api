package com.br.sgs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.br.sgs.models.Terminal;

public interface TerminalRepository extends JpaRepository<Terminal, UUID>{

}

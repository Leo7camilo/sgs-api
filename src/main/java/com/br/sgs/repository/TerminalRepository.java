package com.br.sgs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.Terminal;

@Repository
public interface TerminalRepository extends JpaRepository<Terminal, UUID>{

}

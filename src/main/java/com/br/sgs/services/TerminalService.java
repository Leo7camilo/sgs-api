package com.br.sgs.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.br.sgs.dtos.TerminalDto;
import com.br.sgs.models.TerminalModel;
import com.br.sgs.specifications.SpecificationTemplate.TerminalSpec;

public interface TerminalService {

	TerminalModel save(UUID uuidCompany, TerminalDto terminalDto);

	Optional<TerminalModel> findById(UUID id);

	TerminalModel update(UUID id, TerminalDto terminal);

	boolean existsByName(String name);

	TerminalModel updateStatus(TerminalModel terminalModel);
	
	Page<TerminalModel> getAllTerminal(TerminalSpec spec, Pageable pageable);

}

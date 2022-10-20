package com.br.sgs.services;

import java.util.Optional;
import java.util.UUID;

import com.br.sgs.models.Terminal;

public interface TerminalService {

	Terminal save(UUID uuidCompany, Terminal terminal);

	Optional<Terminal> findById(UUID id);

	Terminal update(UUID id, Terminal terminal);

}

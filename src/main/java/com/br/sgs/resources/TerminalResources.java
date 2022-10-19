package com.br.sgs.resources;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.model.Terminal;
import com.br.sgs.services.TerminalService;

@RestController
@RequestMapping("${api.version}/terminal")
public class TerminalResources {
	
	
	@Autowired
	TerminalService terminalService;
	
	@PostMapping
	private ResponseEntity<Terminal> createTerminal(@PathVariable UUID uuidCompany, @Valid Terminal terminal){
		return new ResponseEntity<Terminal>(terminalService.save(uuidCompany,terminal), HttpStatus.CREATED);
	}

}

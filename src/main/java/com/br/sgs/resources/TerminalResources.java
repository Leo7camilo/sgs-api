package com.br.sgs.resources;

import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.models.Terminal;
import com.br.sgs.services.TerminalService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("${api.version}/terminal")
public class TerminalResources {
	
	
	@Autowired
	TerminalService terminalService;
	
	@PostMapping
	private ResponseEntity<Terminal> createTerminal(@PathVariable UUID uuidCompany, @Valid Terminal terminal){
		return new ResponseEntity<Terminal>(terminalService.save(uuidCompany,terminal), HttpStatus.CREATED);
	}

}

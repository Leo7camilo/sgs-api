package com.br.sgs.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.dtos.UserDto;
import com.br.sgs.models.UserModel;
import com.br.sgs.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("${api.version}/user")
public class UserResources {

	@Autowired
	UserService userService;
	
	@PostMapping
	private ResponseEntity<Object> createUser(@RequestBody @Validated (UserDto.UserView.RegistrationPost.class)
														@JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){
		log.debug("POST registerUser userDto received {} ", userDto.toString());
        if(userService.existsByEmail(userDto.getEmail())){
            log.warn("Email {} is Already Taken ", userDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: Email is Already Taken!");
        }
        UserModel userModel = userService.save(userDto);
        
        log.debug("POST registerUser userId saved {} ", userModel.getUserId());
        log.info("User saved successfully userId {} ", userModel.getUserId());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
	}
}

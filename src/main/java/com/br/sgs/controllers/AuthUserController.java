package com.br.sgs.controllers;

import java.util.UUID;

import javax.validation.Valid;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.config.security.JwtProvider;
import com.br.sgs.dtos.JwtDto;
import com.br.sgs.dtos.LoginDto;
import com.br.sgs.dtos.UserDto;
import com.br.sgs.models.UserModel;
import com.br.sgs.services.UserService;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("${api.version}/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthUserController {

	@Autowired
	UserService userService;
	
	@Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;
	
	@PostMapping("/signup")
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
	
	@GetMapping("/{userId}")
	private ResponseEntity<Object> getUSerById(@PathVariable UUID userId){
		log.info("GET getUSerById userId received 1{} ", userId);
		//log.info("GET getUSerById userId received 2{} ", UUID.fromString(userId));
		return ResponseEntity.status(HttpStatus.CREATED).body(userService.findById(userId));
	}
	
	
	@PostMapping("/login")
    public ResponseEntity<JwtDto> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateJwt(authentication);
        return ResponseEntity.ok(new JwtDto(jwt));
    }
}

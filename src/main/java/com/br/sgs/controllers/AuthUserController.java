package com.br.sgs.controllers;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
import org.springframework.web.bind.annotation.RestController;

import com.br.sgs.config.security.JwtProvider;
import com.br.sgs.dtos.JwtDto;
import com.br.sgs.dtos.LoginDto;
import com.br.sgs.dtos.UserDto;
import com.br.sgs.exception.CompanyNotFound;
import com.br.sgs.exception.DocumentAlredyInUse;
import com.br.sgs.exception.UserNameAlredyInUse;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.TerminalModel;
import com.br.sgs.models.UserModel;
import com.br.sgs.services.CompanyService;
import com.br.sgs.services.UserService;
import com.br.sgs.specifications.SpecificationTemplate;
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
	CompanyService companyService;
	
	@Autowired
    JwtProvider jwtProvider;

    @Autowired
    AuthenticationManager authenticationManager;
	
	@PostMapping("/{companyId}/signup")
	private ResponseEntity<Object> createUser(@PathVariable UUID companyId, @RequestBody @Validated (UserDto.UserView.RegistrationPost.class)
														@JsonView(UserDto.UserView.RegistrationPost.class) UserDto userDto){
		log.debug("POST registerUser userDto received {} ", userDto.toString());
        if(userService.existsByEmail(userDto.getEmail())){
            log.warn("Email {} is Already Taken ", userDto.getEmail());
            throw new DocumentAlredyInUse();
        }
        if(userService.existsByUsername(userDto.getUsername())){
            log.warn("Username {} is Already Taken ", userDto.getUsername());
            throw new UserNameAlredyInUse();
        }
        
        Optional<CompanyModel> company = companyService.findById(companyId);
        if(!company.isPresent()) {
        	log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
        }
        
        UserModel userModel = userService.save(userDto, company.get());
        
        log.debug("POST registerUser userId saved {} ", userModel.getUserId());
        log.info("User saved successfully userId {} ", userModel.getUserId());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(userModel);
	}
	
//	@GetMapping("/{userId}")
//	private ResponseEntity<Object> getUserById(@PathVariable UUID userId){
//		log.info("GET getUSerById userId received {} ", userId);
//		
//		Optional<UserModel> usermodel = userService.findById(userId);
//		
//		if(!usermodel.isPresent()) {
//			throw new NoSuchElementException();
//		}
//		
//		return ResponseEntity.status(HttpStatus.OK).body(usermodel.get());
//	}
	
	@GetMapping("/{companyId}/{userId}")
	private ResponseEntity<Object> getUserByIdAndCompanyId(@PathVariable(value="userId") UUID userId,
			  											  @PathVariable(value="companyId") UUID companyId){
		log.info("GET getUserByIdAndCompanyId userId received {} ", userId);
		log.info("GET getUserByIdAndCompanyId companyId received {} ", companyId);
		
		if (!companyService.existsById(companyId)) {
			log.warn("CompanyId {} not found ", companyId);
			throw new CompanyNotFound();
		}
		
		Optional<UserModel> usermodel = userService.findByIdAndCompanyId(userId, companyId);
		if(!usermodel.isPresent()) {
			throw new NoSuchElementException();
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(usermodel.get());
	}
	
	@GetMapping("/{companyId}")
    public ResponseEntity<Page<UserModel>> getAllUserByCompany(@PathVariable(value="companyId") UUID companyId,
    								SpecificationTemplate.UserSpec spec,
    								@PageableDefault(page = 0, size = 10, sort = "userId", direction = Sort.Direction.ASC) Pageable pageable){
		return ResponseEntity.status(HttpStatus.OK).body(userService.findAllByCompany(SpecificationTemplate.userCompanyId(companyId).and(spec), pageable));
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

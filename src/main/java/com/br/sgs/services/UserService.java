package com.br.sgs.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.br.sgs.dtos.UserDto;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.UserModel;

public interface UserService {

	UserModel save(UserDto userDto);

	boolean existsByEmail(String email);
	boolean existsByUsername(String username);
	
	Optional<UserModel> findById(UUID userId);
	
	Optional<UserModel> findByUsername(String username);

	Optional<UserModel> findByIdAndCompanyId(UUID userId,  UUID companyId);

	UserModel save(UserDto userDto, CompanyModel companyModel);

	Page<UserModel> findAllByCompany(Specification<UserModel> and, Pageable pageable);

	


}

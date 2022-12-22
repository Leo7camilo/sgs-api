package com.br.sgs.services;

import java.util.Optional;
import java.util.UUID;

import com.br.sgs.dtos.UserDto;
import com.br.sgs.models.UserModel;

public interface UserService {

	UserModel save(UserDto userDto);

	boolean existsByEmail(String email);
	
	Optional<UserModel> findById(UUID userId);
	
	Optional<UserModel> findByUsername(String username);

	Optional<UserModel> findByIdAndCompanyId(UUID userId,  UUID companyId);


}

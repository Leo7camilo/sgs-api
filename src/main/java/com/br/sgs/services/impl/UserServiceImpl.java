package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.br.sgs.dtos.UserDto;
import com.br.sgs.enums.UserStatus;
import com.br.sgs.enums.UserType;
import com.br.sgs.models.UserModel;
import com.br.sgs.repository.UserRepository;
import com.br.sgs.services.UserService;

public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;
	
	@Autowired
    PasswordEncoder passwordEncoder;

	@Override
	public UserModel save(UserDto userDto) {
		userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
		
		var userModel = new UserModel();
		BeanUtils.copyProperties(userDto, userModel);

		userModel.setUserStatus(UserStatus.ACTIVE);
		userModel.setUserType(UserType.EMPLOYEE);
		userModel.setDtCreated(LocalDateTime.now(ZoneId.of("UTC")));
		userModel.setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		return userRepository.save(userModel);
	}

	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}

}

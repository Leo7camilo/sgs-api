package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.UserDto;
import com.br.sgs.enums.UserStatus;
import com.br.sgs.enums.UserType;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.UserModel;
import com.br.sgs.repository.UserRepository;
import com.br.sgs.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserModel save(UserDto userDto) {
		
		userDto.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		
		var userModel = new UserModel();
		BeanUtils.copyProperties(userDto, userModel);

		userModel.setUserStatus(UserStatus.ACTIVE);
		userModel.setUserType(UserType.EMPLOYEE);
		userModel.setDtCreated(LocalDateTime.now(ZoneId.of("UTC")));
		userModel.setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		return userRepository.save(userModel);
	}
	
	@Override
	public UserModel save(UserDto userDto, CompanyModel companyModel) {
		
		userDto.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
		
		var userModel = new UserModel();
		BeanUtils.copyProperties(userDto, userModel);

		userModel.setUserStatus(UserStatus.ACTIVE);
		userModel.setUserType(UserType.EMPLOYEE);
		userModel.setDtCreated(LocalDateTime.now(ZoneId.of("UTC")));
		userModel.setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		userModel.setCompany(companyModel);
		return userRepository.save(userModel);
	}
	
	@Override
	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	@Override
	public boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}

	@Override
	public Optional<UserModel> findById(UUID userId) {
		return userRepository.findById(userId);
	}

	@Override
	public Optional<UserModel> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public Optional<UserModel> findByIdAndCompanyId(UUID userId, UUID companyId) {
		return userRepository.findByUserIdAndCompanyCompanyId(userId, companyId);
	}

	@Override
	public Page<UserModel> findAllByCompany(Specification<UserModel> spec, Pageable pageable) {
		return userRepository.findAll(spec, pageable);
	}	

}

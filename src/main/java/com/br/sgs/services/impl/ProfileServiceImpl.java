package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.ProfileDto;
import com.br.sgs.enums.ProfileState;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.ProfileModel;
import com.br.sgs.repository.CompanyRepository;
import com.br.sgs.repository.ProfileRepository;
import com.br.sgs.repository.TerminalRepository;
import com.br.sgs.services.ProfileService;


@Service
public class ProfileServiceImpl implements ProfileService{
	
	@Autowired
	ProfileRepository profileRepository;
	
	@Autowired
	TerminalRepository terminalRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	

	@Override
	public ProfileModel save(UUID uuidCompany, ProfileDto profileDto) {
		Optional<CompanyModel> company = companyRepository.findById(uuidCompany);
		if(!company.isPresent()) {
			throw new NoSuchElementException();
		}
		
		ProfileModel profileModel = new ProfileModel();
		BeanUtils.copyProperties(profileDto, profileModel);
		profileModel.setCompany(company.get());
		profileModel.setDtChange(LocalDateTime.now(ZoneId.of("UTC")));
		profileModel.setStatus(ProfileState.ACTIVE);
		
		return profileRepository.save(profileModel);
	}

	@Override
	public boolean existsByDescription(String description) {
		return profileRepository.existsByDescription(description);
	}

	@Override
	public ProfileModel updateStatus(ProfileModel profile) {
		if(profile.getStatus().equals(ProfileState.ACTIVE))
			profile.setStatus(ProfileState.DISABLE);
		else
			profile.setStatus(ProfileState.ACTIVE);
			
		profile.setDtChange(LocalDateTime.now(ZoneId.of("UTC")));
		return profileRepository.save(profile);
	}

	@Override
	public Page<ProfileModel> findAllByCompany(Specification<ProfileModel> spec, Pageable pageable) {
		return profileRepository.findAll(spec, pageable);
	}

	@Override
	public Optional<ProfileModel> findById(UUID id) {
		return profileRepository.findById(id);
	}

	@Override
	public Optional<ProfileModel> findByIdAndCompanyId(UUID profileId, UUID companyId) {
		return profileRepository.findByProfileIdAndCompanyCompanyId(profileId, companyId);
	}

	
}

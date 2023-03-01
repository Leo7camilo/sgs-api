package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.sgs.dtos.ProfileDto;
import com.br.sgs.enums.ProfileState;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.ProfileModel;
import com.br.sgs.models.QueueModel;
import com.br.sgs.repository.CompanyRepository;
import com.br.sgs.repository.ProfileRepository;
import com.br.sgs.repository.TerminalRepository;
import com.br.sgs.services.ProfileService;
import com.br.sgs.services.QueueService;


@Service
public class ProfileServiceImpl implements ProfileService{
	
	@Autowired
	ProfileRepository profileRepository;
	
	@Autowired
	TerminalRepository terminalRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	QueueService queueService;
	

	@Override
	public ProfileModel save(UUID uuidCompany, ProfileDto profileDto) {
		Optional<CompanyModel> company = companyRepository.findById(uuidCompany);
		if(!company.isPresent()) {
			throw new NoSuchElementException();
		}
		
		ProfileModel profileModel = new ProfileModel();
		profileModel.setDescription(profileDto.getDescription());
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

	@Override
	@Transactional
	public ProfileModel grantPermission(ProfileModel profileModel, ProfileDto profileDto) {
		Set<QueueModel> queues = new HashSet<>();
	
		for(UUID id :profileDto.getQueues()) {
			Optional<QueueModel> queue = queueService.findById(id);
			if(queue.isPresent())
				queues.add(queue.get());
		}
		profileModel.setQueues(queues);
		return profileRepository.save(profileModel);
	}

	@Override
	public ProfileModel removePermission(ProfileModel profileModel, UUID queueId) {
		
		Set<QueueModel> queues = profileModel.getQueues();
		System.out.println("antes");
		queues.forEach(x -> System.out.println(x));
		
		for(QueueModel queue : queues) {
			if(queue.getQueueId().equals(queueId)) {
				System.out.println("oi");
				queues.remove(queue);
			}
		}
		System.out.println("depois");
		queues.forEach(x -> System.out.println(x));
		
		profileModel.setQueues(queues);
		
		return profileRepository.save(profileModel);
	}

	
}

package com.br.sgs.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.br.sgs.dtos.ProfileDto;
import com.br.sgs.models.ProfileModel;

public interface ProfileService {

	ProfileModel save(UUID uuidCompany, ProfileDto profileDto);

	Optional<ProfileModel> findById(UUID id);

	boolean existsByDescription(String description);

	ProfileModel updateStatus(ProfileModel profileModel);

	Optional<ProfileModel> findByIdAndCompanyId(UUID profileId, UUID companyId);

	Page<ProfileModel> findAllByCompany(Specification<ProfileModel> spec, Pageable pageable);

	ProfileModel grantPermission(ProfileModel profileModel, ProfileDto profileDto);

	ProfileModel removePermission(ProfileModel profileModel, UUID queueId);

}

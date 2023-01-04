package com.br.sgs.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.ProfileModel;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileModel, UUID>, JpaSpecificationExecutor<ProfileModel> {
	boolean existsByDescription(String description);
	
	Optional<ProfileModel> findByDescriptionAndCompanyCompanyId(String description, UUID companyId);

	Optional<ProfileModel> findByProfileIdAndCompanyCompanyId(UUID profileId, UUID companyId);
}

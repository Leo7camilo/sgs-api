package com.br.sgs.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.AttendenceModel;

@Repository
public interface AttendenceRepository
		extends JpaRepository<AttendenceModel, UUID>, JpaSpecificationExecutor<AttendenceModel> {
	boolean existsByName(String name);

	Optional<AttendenceModel> findByIdQueue(UUID idQueue);

}

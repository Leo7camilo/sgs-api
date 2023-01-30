package com.br.sgs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.AttendenceHistModel;

@Repository
public interface AttendenceHistRepository
		extends JpaRepository<AttendenceHistModel, UUID>, JpaSpecificationExecutor<AttendenceHistModel> {
}

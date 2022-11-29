package com.br.sgs.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.br.sgs.dtos.AttendenceDto;
import com.br.sgs.models.AttendenceModel;
import com.br.sgs.specifications.SpecificationTemplate.AttendenceSpec;

public interface AttendenceService {

	Page<AttendenceModel> getAllTerminal(AttendenceSpec spec, Pageable pageable);

	void save(AttendenceDto attendenceDto, UUID idCompany, UUID idClient);

}

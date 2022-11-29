package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.sgs.dtos.AttendenceDto;
import com.br.sgs.enums.AttendenceState;
import com.br.sgs.models.AttendenceModel;
import com.br.sgs.models.ClientModel;
import com.br.sgs.repository.AttendenceRepository;
import com.br.sgs.services.AttendenceService;
import com.br.sgs.specifications.SpecificationTemplate.AttendenceSpec;


@Service
public class AttendenceServiceImpl implements AttendenceService{
	
	@Autowired
	AttendenceRepository attendenceRepository;
	
	@Override
	public Page<AttendenceModel> getAllTerminal(AttendenceSpec spec, Pageable pageable) {
		return attendenceRepository.findAll(spec, pageable);
	}

	@Transactional
	@Override
	public void save(AttendenceDto attendenceDto, UUID idCompany, UUID idClient) {
	
		AttendenceModel attendenceModel = new AttendenceModel();
		attendenceModel.setDtCreated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.setIdClient(idClient);
		attendenceModel.setIdCompany(idCompany);
		attendenceModel.setStatus(AttendenceState.WAITING);
		
		for(UUID idQueue: attendenceDto.getIdQueueList()) {
			attendenceModel.setIdQueue(idQueue);
			attendenceRepository.save(attendenceModel);
		}
	}
	

	
}

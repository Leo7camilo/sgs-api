package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.sgs.dtos.AttendenceDto;
import com.br.sgs.enums.AttendenceState;
import com.br.sgs.models.AttendenceModel;
import com.br.sgs.models.QueueModel;
import com.br.sgs.repository.AttendenceRepository;
import com.br.sgs.services.AttendenceService;
import com.br.sgs.services.QueueService;
import com.br.sgs.specifications.SpecificationTemplate.AttendenceSpec;


@Service
public class AttendenceServiceImpl implements AttendenceService{
	
	@Autowired
	AttendenceRepository attendenceRepository;
	
	@Autowired
	QueueService queueService;
	
	@Override
	public Page<AttendenceModel> getAllAttendence(AttendenceSpec spec, Pageable pageable) {
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
		attendenceModel.setStatus(AttendenceState.NOT_FIT);
		attendenceModel.setPassword(attendenceDto.getPassaword());
		
		List<QueueModel> listOrdened = queueService.orderListQueueByPriority(attendenceDto.getIdQueueList());
		for(int i = 0; i<listOrdened.size(); i++) {
			if(i == 0)
				attendenceModel.setStatus(AttendenceState.WAITING);
			attendenceModel.setIdQueue(listOrdened.get(i).getQueueId());
			attendenceRepository.save(attendenceModel);
		}
	}
	
	@Override
	public Optional<AttendenceModel> findByIdQueue(UUID idQueue) {
		return attendenceRepository.findByIdQueue(idQueue);
	}

	@Override
	public void updateStatus(AttendenceDto attendenceDto, UUID idCompany, UUID idQueue) {
		Optional<AttendenceModel> attendenceModel = findByIdQueue(idQueue);
		if(!attendenceModel.isPresent()) {
			throw new NoSuchElementException();
		}
		
		if(!attendenceModel.get().getStatus().equals(AttendenceState.WAITING)) {
			//tentativa de chamar usuário antes da ordem, ou em atendimento ou já atendido
			throw new NoSuchElementException();
		}
		
		if(attendenceModel.get().getIdCompany() != idCompany) {
			//tentativa de alterar fila de outra companhia
			throw new NoSuchElementException();
		}
		
		attendenceModel.get().setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.get().setStatus(AttendenceState.IN_ATTENDENCE);
		attendenceRepository.save(attendenceModel.get());
	}

	@Override
	@Transactional
	public void updateStatus(UUID idCompany, UUID idQueue, UUID idClient) {
		Optional<AttendenceModel> attendenceModel = findByIdQueue(idQueue);
		if(!attendenceModel.isPresent()) {
			throw new NoSuchElementException();
		}
		attendenceModel.get().setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.get().setStatus(AttendenceState.ATTENDED);
		attendenceRepository.save(attendenceModel.get());
		
		List<AttendenceModel> attendenceModelList = attendenceRepository.findByIdClientOrderByIdAttendence(idClient);
		if(attendenceModelList != null && attendenceModelList.size() > 0) {
			attendenceModelList.get(0).setStatus(AttendenceState.WAITING);
			attendenceRepository.save(attendenceModelList.get(0));
		}
		
	}


}

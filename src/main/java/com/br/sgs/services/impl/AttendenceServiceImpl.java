package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.sgs.dtos.AttendenceDto;
import com.br.sgs.enums.AttendenceState;
import com.br.sgs.models.AttendenceModel;
import com.br.sgs.models.ClientModel;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.QueueModel;
import com.br.sgs.models.TerminalModel;
import com.br.sgs.repository.AttendenceRepository;
import com.br.sgs.services.AttendenceService;
import com.br.sgs.services.QueueService;
import com.br.sgs.specifications.SpecificationTemplate.AttendenceSpec;

@Service
public class AttendenceServiceImpl implements AttendenceService {

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
	public void save(AttendenceDto attendenceDto, CompanyModel company, ClientModel client) {
		
		AttendenceModel attendenceModel = new AttendenceModel();
		attendenceModel = setAtributes(attendenceDto, company, client, attendenceModel);

		List<QueueModel> listOrdened = queueService.orderListQueueByPriority(attendenceDto.getIdQueueList());
		for (int i = 0; i < listOrdened.size(); i++) {
			if (i == 0)
				attendenceModel.setStatus(AttendenceState.WAITING);
			else {
				attendenceModel = new AttendenceModel();
				attendenceModel = setAtributes(attendenceDto, company, client, attendenceModel);
			}
			attendenceModel.setIdQueue(listOrdened.get(i).getQueueId());
			attendenceRepository.save(attendenceModel);
		}
	}

	

	@Override
	public Optional<AttendenceModel> findByIdQueue(UUID idQueue) {
		return attendenceRepository.findByIdQueue(idQueue);
	}

	@Override
	public void updateStatus(AttendenceDto attendenceDto, UUID idCompany, UUID idQueue, TerminalModel terminal) {
		Optional<AttendenceModel> attendenceModel = findByIdQueue(idQueue);
		if (!attendenceModel.isPresent()) {
			throw new NoSuchElementException();
		}

		if (!attendenceModel.get().getStatus().equals(AttendenceState.WAITING)) {
			// tentativa de chamar usuário antes da ordem, ou em atendimento ou já atendido
			throw new NoSuchElementException();
		}

		if (!attendenceModel.get().getCompany().getCompanyId().equals(idCompany)) {
			// tentativa de alterar fila de outra companhia
			throw new NoSuchElementException();
		}

		attendenceModel.get().setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.get().setStatus(AttendenceState.IN_ATTENDENCE);
		attendenceModel.get().setTerminal(terminal);
		attendenceRepository.save(attendenceModel.get());
	}

	@Override
	@Transactional
	public void updateStatus(UUID idCompany, UUID idQueue, UUID idClient) {
		Optional<AttendenceModel> attendenceModel = findByIdQueue(idQueue);
		if (!attendenceModel.isPresent()) {
			throw new NoSuchElementException();
		}
		attendenceModel.get().setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.get().setStatus(AttendenceState.ATTENDED);
		attendenceRepository.save(attendenceModel.get());

		List<AttendenceModel> attendenceModelList = attendenceRepository
				.findByClientClientIdOrderByAttendenceId(idClient);
		if (attendenceModelList != null && attendenceModelList.size() > 0) {
			attendenceModelList.get(0).setStatus(AttendenceState.WAITING);
			attendenceRepository.save(attendenceModelList.get(0));
		}

	}

	@Override
	@Transactional
	public void updateStatus(UUID companyId, TerminalModel terminal, AttendenceModel attendenceModel) {

		attendenceModel.setTerminal(terminal);
		attendenceModel.setStatus(AttendenceState.IN_ATTENDENCE);

		attendenceRepository.save(attendenceModel);
	}

	@Override
	public Integer getNextPassword(UUID companyId) {
		return attendenceRepository.findMaxPasswordByCompanyCompanyIdAndDtCreatedBetween(companyId.toString(),
				LocalDateTime.now(ZoneId.of("UTC")).with(LocalTime.MIDNIGHT),
				LocalDateTime.now(ZoneId.of("UTC")).with(LocalTime.MAX));
	}

	@Override
	public Page<AttendenceModel> getAllAttendence(Specification<AttendenceModel> spec, Pageable pageable) {
		return attendenceRepository.findAll(spec, pageable);
	}

	@Override
	public void save(AttendenceDto attendenceDto, CompanyModel companyModel) {
		AttendenceModel attendenceModel = new AttendenceModel();
		attendenceModel.setPassword(attendenceDto.getPassaword());
		attendenceModel.setCompany(companyModel);
		attendenceModel.setDtCreated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.setStatus(AttendenceState.WAITING);

		attendenceRepository.save(attendenceModel);
	}

	@Override
	public Optional<AttendenceModel> findByIdAndCompanyId(UUID attendenceId, UUID companyId) {
		return attendenceRepository.findByAttendenceIdAndCompanyCompanyId(attendenceId, companyId);
	}

	private AttendenceModel findByPasswordAndCompanyId(AttendenceDto attendenceDto, CompanyModel company) {

		Optional<AttendenceModel> attendence = attendenceRepository
				.findByPasswordAndCompanyCompanyId(attendenceDto.getPassaword(), company.getCompanyId());

		return attendence.isPresent() ? attendence.get() : new AttendenceModel();
	}
	
	private AttendenceModel setAtributes(AttendenceDto attendenceDto, CompanyModel company, ClientModel client,
			AttendenceModel attendenceModel) {
		
		
		attendenceModel.setDtCreated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.setClient(client);
		attendenceModel.setCompany(company);
		attendenceModel.setStatus(AttendenceState.NOT_FIT);
		attendenceModel.setPassword(attendenceDto.getPassaword());
		
		return attendenceModel;
	}

}

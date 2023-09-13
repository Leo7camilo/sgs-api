package com.br.sgs.services.impl;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.br.sgs.dtos.AttendenceDto;
import com.br.sgs.dtos.AttendenceGroupDateDto;
import com.br.sgs.dtos.DefaultValueDto;
import com.br.sgs.enums.AttendenceState;
import com.br.sgs.models.AttendenceModel;
import com.br.sgs.models.ClientModel;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.QueueModel;
import com.br.sgs.models.TerminalModel;
import com.br.sgs.repository.AttendenceRepository;
import com.br.sgs.services.AttendenceHistService;
import com.br.sgs.services.AttendenceService;
import com.br.sgs.services.ClientService;
import com.br.sgs.services.QueueService;
import com.br.sgs.specifications.SpecificationTemplate.AttendenceSpec;

@Service
public class AttendenceServiceImpl implements AttendenceService {

	@Autowired
	AttendenceRepository attendenceRepository;
	
	@Autowired
	AttendenceHistService attendenceHistService;

	@Autowired
	QueueService queueService;
	
	@Autowired
	ClientService clientService;

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
			attendenceModel.setQueue(listOrdened.get(i));
			
			saveHistory(attendenceRepository.save(attendenceModel));
		}
	}

	@Override
	public void updateStatus(AttendenceDto attendenceDto, UUID idCompany, UUID idQueue, TerminalModel terminal) {
		
		Optional<AttendenceModel> attendenceModel = findByAttendenceId(attendenceDto.getAttendenceId());
		//Optional<AttendenceModel> attendenceModel = findByIdQueue(idQueue);
		if (!attendenceModel.isPresent()) {
			throw new NoSuchElementException();
		}

		/*if (!attendenceModel.get().getStatus().equals(AttendenceState.WAITING)) {
			// tentativa de chamar usuário antes da ordem, ou em atendimento ou já atendido
			throw new NoSuchElementException();
		}*/

		if (!attendenceModel.get().getCompany().getCompanyId().equals(idCompany)) {
			// tentativa de alterar fila de outra companhia
			throw new NoSuchElementException();
		}

		attendenceModel.get().setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.get().setStatus(AttendenceState.IN_ATTENDENCE);
		attendenceModel.get().setTerminal(terminal);
		
		saveHistory(attendenceRepository.save(attendenceModel.get()));
	}

	@Override
	@Transactional
	public void updateStatus(UUID idCompany, UUID idQueue, UUID idClient, UUID attendenceId) {
		//Optional<AttendenceModel> attendenceModel = findByIdQueue(idQueue);
		Optional<AttendenceModel> attendenceModel = updateStatusToAttended(attendenceId, idClient);
		
		List<AttendenceModel> attendenceModelList = attendenceRepository
				.findByClientClientIdAndQueueQueueIdNotOrderByAttendenceId(idClient, idQueue);
		if (attendenceModelList != null && attendenceModelList.size() > 0) {
			attendenceModelList.get(0).setStatus(AttendenceState.WAITING);
			saveHistory(attendenceRepository.save(attendenceModelList.get(0)));
		}
		attendenceRepository.deleteById(attendenceModel.get().getAttendenceId());
	}

	private Optional<AttendenceModel> updateStatusToAttended(UUID attendenceId, UUID idClient) {
		
		Optional<AttendenceModel> attendenceModel = findByAttendenceId(attendenceId);
		if (!attendenceModel.isPresent()) {
			throw new NoSuchElementException();
		}
		
		Optional<ClientModel> client = clientService.findById(idClient);
		if(client.isPresent()) {
			attendenceModel.get().setClient(client.get());
		}
		
		attendenceModel.get().setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.get().setStatus(AttendenceState.ATTENDED);
		saveHistory(attendenceRepository.save(attendenceModel.get()));
		return attendenceModel;
	}

	private Optional<AttendenceModel> updateStatusToAttended(UUID attendenceId) {
		Optional<AttendenceModel> attendenceModel = findByAttendenceId(attendenceId);
		if (!attendenceModel.isPresent()) {
			throw new NoSuchElementException();
		}
		attendenceModel.get().setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.get().setStatus(AttendenceState.ATTENDED);
		saveHistory(attendenceRepository.save(attendenceModel.get()));
		return attendenceModel;
	}

	@Override
	@Transactional
	public void updateStatus(UUID companyId, TerminalModel terminal, AttendenceModel attendenceModel) {

		attendenceModel.setTerminal(terminal);
		attendenceModel.setStatus(AttendenceState.IN_ATTENDENCE);
		
		saveHistory(attendenceRepository.save(attendenceModel));
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
		attendenceModel.setPassword(attendenceDto.getPassword());
		attendenceModel.setCompany(companyModel);
		attendenceModel.setDtCreated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.setStatus(AttendenceState.WAITING);
		
		Optional<QueueModel> queueAtendimento = queueService.findByCompanyIdAndDescription(companyModel.getCompanyId(), "ATENDIMENTO");
		if(queueAtendimento.isPresent()) {
			attendenceModel.setQueue(queueAtendimento.get());
		}

		saveHistory(attendenceRepository.save(attendenceModel));
	}

	@Override
	public Optional<AttendenceModel> findByIdAndCompanyId(UUID attendenceId, UUID companyId) {
		return attendenceRepository.findByAttendenceIdAndCompanyCompanyId(attendenceId, companyId);
	}
	
	@Override
	public Optional<AttendenceModel> findByIdQueue(UUID idQueue) {
		return attendenceRepository.findByQueueQueueId(idQueue);
	}

	@Override
	public void updateStatus(UUID companyId, UUID queueId, UUID attendenceId) {
		Optional<AttendenceModel> attendenceModel = updateStatusToAttended(attendenceId);
		attendenceRepository.deleteById(attendenceModel.get().getAttendenceId());
	}

	private AttendenceModel findByPasswordAndCompanyId(AttendenceDto attendenceDto, CompanyModel company) {

		Optional<AttendenceModel> attendence = attendenceRepository
				.findByPasswordAndCompanyCompanyId(attendenceDto.getPassword(), company.getCompanyId());

		return attendence.isPresent() ? attendence.get() : new AttendenceModel();
	}
	
	private AttendenceModel setAtributes(AttendenceDto attendenceDto, CompanyModel company, ClientModel client,
			AttendenceModel attendenceModel) {
		
		attendenceModel.setDtCreated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.setDtUpdated(LocalDateTime.now(ZoneId.of("UTC")));
		attendenceModel.setClient(client);
		attendenceModel.setCompany(company);
		attendenceModel.setStatus(AttendenceState.NOT_FIT);
		attendenceModel.setPassword(attendenceDto.getPassword());
		
		return attendenceModel;
	}
	private void saveHistory(AttendenceModel attendenceModel) {
		attendenceHistService.save(attendenceModel);
	}

	private Optional<AttendenceModel> findByAttendenceId(UUID attendenceId) {
		return attendenceRepository.findByAttendenceId(attendenceId);
	}

	@Override
	public Object countAttendenceByCompany(UUID companyId) {
		Set<AttendenceState> status = new HashSet<>();
		status.add(AttendenceState.ATTENDED);
		
		DefaultValueDto defaultValueDto = new DefaultValueDto();
		defaultValueDto.setValue(attendenceRepository.countByCompanyCompanyIdAndStatusNotIn(companyId, status).toString());
		defaultValueDto.setDescription("Total de clientes na estabelicimento");
		return defaultValueDto;
	}

	@Override
	public Object countAttendenceByCompanyAndDate(UUID companyId) {
		DefaultValueDto defaultValueDto = new DefaultValueDto();
		
		LocalDate iniDate = LocalDate.now(ZoneId.of("UTC"));
		LocalDate endDate = LocalDate.now(ZoneId.of("UTC")).plusDays(1);
		
		//Long value = attendenceRepository.countByCompanyCompanyIdAndDtCreatedBetween(
			//	companyId, iniDate.atStartOfDay(), endDate.atStartOfDay());

		Long value = attendenceHistService.countByCompanyCompanyIdAndDtCreatedBetween(
				companyId, iniDate.atStartOfDay(), endDate.atStartOfDay());
		defaultValueDto.setValue(value.toString());
		defaultValueDto.setDescription("Atendimentos Concluídos");
		return defaultValueDto;
	}	
	
	@Override
	public Object findMostFrequentAttendenceQueue(UUID companyId) {
		DefaultValueDto defaultValueDto = new DefaultValueDto();
		defaultValueDto.setDescription("Fila com mais atendimento");

		List<String> queueDescriptions = attendenceRepository.findMostFrequentAttendenceQueue(companyId.toString());
		if(!queueDescriptions.isEmpty()) 
			defaultValueDto.setValue(queueDescriptions.get(0));
		
		return defaultValueDto;
	}

	@Override
	public List<AttendenceGroupDateDto> groupAttendenceByCompanyAndDate(UUID companyId) {
		List<AttendenceGroupDateDto> listAttendenceGroupDateDto = new ArrayList<>();
		
		List<QueueModel> queues = queueService.findByCompanyId(companyId);
		List<AttendenceGroupDateDto> attendenceGroupDateDtoList = heapAttendenceGroup(queues, LocalDate.now(ZoneId.of("UTC")));
		List<Object[]> listObj = attendenceRepository.groupAttendenceByCompanyAndDate(companyId.toString(), LocalDate.now(ZoneId.of("UTC")));
		if(listObj != null && !listObj.isEmpty()){
			listObj.forEach(attendenceGroupDateDtoResult -> {
				AttendenceGroupDateDto attendenceGroupDateDto = new AttendenceGroupDateDto();
				attendenceGroupDateDto.setDescription(attendenceGroupDateDtoResult[0].toString());
				attendenceGroupDateDto.setCount((BigInteger)attendenceGroupDateDtoResult[1]);
				
				LocalDateTime ldt = LocalDateTime.parse(attendenceGroupDateDtoResult[2].toString().replace(" ","T"));
				attendenceGroupDateDto.setHour(ldt);
				listAttendenceGroupDateDto.add(attendenceGroupDateDto);
			});
		}
		List<AttendenceGroupDateDto> attendenceGroupDateDtoListResult = new ArrayList<>();
		boolean insert = false;
		for(AttendenceGroupDateDto attendence :attendenceGroupDateDtoList) {
			for(AttendenceGroupDateDto attendenceData :listAttendenceGroupDateDto) {
				if(attendence.getDescription().equals(attendenceData.getDescription()) && attendence.getHour().equals(attendenceData.getHour())) {
					attendenceGroupDateDtoListResult.add(attendenceData);
					insert = true;
				}
			}
			if(!insert) {
				attendenceGroupDateDtoListResult.add(attendence);
			}
			insert = false;
		}
		
		return attendenceGroupDateDtoListResult;
	}

	private List<AttendenceGroupDateDto> heapAttendenceGroup(List<QueueModel> queues, LocalDate now) {
		List<AttendenceGroupDateDto> listAttendenceGroupDateDto = new ArrayList<>();
		
		queues.forEach(queue -> {
			for(int i = 6; i <= LocalDateTime.MAX.getHour() - 4; i++) {
				AttendenceGroupDateDto attendenceGroupDateDto = new AttendenceGroupDateDto();
				attendenceGroupDateDto.setDescription(queue.getDescription());
				attendenceGroupDateDto.setCount(BigInteger.ZERO);
				attendenceGroupDateDto.setHour(now.atStartOfDay().plusHours(i));	
				listAttendenceGroupDateDto.add(attendenceGroupDateDto);			
			}
		});		
		return listAttendenceGroupDateDto;
	}	
	
}

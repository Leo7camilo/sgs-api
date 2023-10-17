package com.br.sgs.services.impl;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.EngagementClientDetailedDto;
import com.br.sgs.dtos.EngagementClientDto;
import com.br.sgs.dtos.EngagementClientSumarizedDto;
import com.br.sgs.enums.ClientEngagementLogState;
import com.br.sgs.models.ClientEngagementLogModel;
import com.br.sgs.repository.ClientEngagementLogRepository;
import com.br.sgs.services.ClientEngagementLogService;
import com.br.sgs.services.ClientService;
import com.br.sgs.services.CompanyService;


@Service
public class ClientEngagementLogServiceImpl implements ClientEngagementLogService{
	 
	@Autowired
	ClientEngagementLogRepository clientEngagementLogRepository;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	ClientService clientService;

	@Override
	public boolean existsByClientIdAndCompanyId(UUID clientId, UUID companyId) {
		return clientEngagementLogRepository.existsByClientClientIdAndCompanyCompanyId(clientId, companyId);
	}

	@Override
	public boolean existsByClientIdAndDtCreatedAndCompanyId(UUID clientId, LocalDate dtCreated, UUID companyId) {
		return clientEngagementLogRepository.existsByClientClientIdAndDtCreatedAndCompanyCompanyId(clientId, dtCreated, companyId);
	}


	@Override
	public ClientEngagementLogModel save(ClientEngagementLogModel clientEngagementLog) {
		return clientEngagementLogRepository.save(clientEngagementLog);
	}

	@Override
	public void validateClientByClientIdAndCompanyId(UUID clientId, UUID companyId) {
		
		if(clientId != null) {
			if(existsByClientIdAndCompanyId(clientId, companyId)) {
				if(!existsByClientIdAndDtCreatedAndCompanyId(clientId, LocalDate.now(), companyId)) {
					createClientEngagementLog(clientId, companyId, false);
				}
			}else {
				createClientEngagementLog(clientId, companyId, true);
			}
		}
		
	}

	private void createClientEngagementLog(UUID clientId, UUID companyId, boolean novo) {
		ClientEngagementLogModel clientEngagementLogModel = new ClientEngagementLogModel();
		clientEngagementLogModel.setCompany(companyService.findById(companyId).get());
		clientEngagementLogModel.setClient(clientService.findById(clientId).get());
		
		clientEngagementLogModel.setDtCreated(LocalDate.now());

		clientEngagementLogModel.setStatus(novo ? ClientEngagementLogState.NEW : ClientEngagementLogState.KNOWN);
		save(clientEngagementLogModel);
	}

	@Override
	public Object getEngagementClient(UUID companyId) {	
		EngagementClientDto engagementClientDto = new EngagementClientDto();
		
		List<Object[]> clientEngagementLogSumarized = clientEngagementLogRepository
				.getSumarizedClientEngagementByCompanyIdAndDtCreatedBetween(companyId.toString(), LocalDate.now().minusYears(1), LocalDate.now());
		if(clientEngagementLogSumarized != null && !clientEngagementLogSumarized.isEmpty()) {
			clientEngagementLogSumarized.forEach(clientEngagementLog -> {
				
				EngagementClientSumarizedDto engagementClientSumarizedDto = new EngagementClientSumarizedDto();
				engagementClientSumarizedDto.setStatus(clientEngagementLog[0].toString());
				engagementClientSumarizedDto.setCount(((BigInteger) clientEngagementLog[1]));

				engagementClientDto.getListEngagementClientSumarized().add(engagementClientSumarizedDto);
			});
		}
		List<Object[]> clientEngagementLogDetailed = clientEngagementLogRepository
				.getDetailedClientEngagementByDtCreatedBetweenAndCompanyId(LocalDate.now(), companyId.toString());
		if(clientEngagementLogDetailed != null && !clientEngagementLogDetailed.isEmpty()) {
			clientEngagementLogDetailed.forEach(clientEngagementLog -> {
				EngagementClientDetailedDto engagementClientDetailedDto = new EngagementClientDetailedDto();
				engagementClientDetailedDto.setStatus(clientEngagementLog[0].toString());		
				
				String date = LocalDate.now().getYear() + "-" + String.format("%02d", (BigInteger)clientEngagementLog[1]) + "-" + "01";
				LocalDate mounth = LocalDate.parse(date);
				
				engagementClientDetailedDto.setDate(mounth);
				engagementClientDetailedDto.setCount((BigInteger)clientEngagementLog[2]);	
				
				engagementClientDto.getListEngagementClientDetailed().add(engagementClientDetailedDto);
			});			
		}
		
		return engagementClientDto;
	}
	
}

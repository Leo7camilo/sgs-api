package com.br.sgs.services;

import java.time.LocalDate;
import java.util.UUID;

import com.br.sgs.models.ClientEngagementLogModel;

public interface ClientEngagementLogService {
	
	boolean existsByClientIdAndCompanyId(UUID clientId, UUID comapanyId);
	boolean existsByClientIdAndDtCreatedAndCompanyId(UUID clientId, LocalDate dtCreated, UUID comapanyId);
	ClientEngagementLogModel save(ClientEngagementLogModel clientDto);
	void validateClientByClientIdAndCompanyId(UUID clientId, UUID companyId);
	Object getEngagementClient(UUID companyId);
}

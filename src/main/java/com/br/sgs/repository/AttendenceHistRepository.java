package com.br.sgs.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.br.sgs.models.AttendenceHistModel;


public interface AttendenceHistRepository
		extends JpaRepository<AttendenceHistModel, UUID>, JpaSpecificationExecutor<AttendenceHistModel> {

	Long countByCompanyCompanyIdAndDtCreatedBetween(UUID companyId, LocalDateTime atStartOfDay,
			LocalDateTime atStartOfDay2);
	
	@Query(value = 
	" select c.organization, \r\n"
    + "count(distinct ath.attendence_id) as count, \r\n"
	+ "   (select count(distinct atth.attendence_id) from attendence_hist atth \r\n"
	+ "    where atth.company_id = :companyId) as total \r\n"
	+ "from attendence_hist ath \r\n"
	+ "join client c    on ath.client_id  = c.client_id \r\n"
	+ "join company com on ath.company_id = com.company_id \r\n"
	+ "where ath.company_id = :companyId \r\n"
	+ "group by c.organization \r\n"
	+ "limit 3", nativeQuery = true)
	List<Object[]> mostCompanyClientByCompanyId(UUID companyId);
}

package com.br.sgs.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
	+ "order by count desc \r\n"
	+ "limit 3 ", nativeQuery = true)
	List<Object[]> mostCompanyClientByCompanyId(@Param("companyId")String companyId);
	
	@Query(value = 
			"select c.name  as client_name, "
			+ "		q.description, "
			+ "		DATE_FORMAT(ah.dt_created, '%Y-%m-%d %H:%i:%s') AS dt_created, \r\n"
			+ "     DATE_FORMAT(ah.dt_updated, '%Y-%m-%d %H:%i:%s') AS dt_updated, \r\n"
			+ "		t.name  as terminal"
			+ "   from sgsapi.attendence_hist ah \r\n"
			+ "	  join sgsapi.queue q    on ah.queue_id = q.queue_id \r\n"
			+ "	  join sgsapi.client c   on ah.client_id = c.client_id \r\n"
			+ "   join sgsapi.terminal t on ah.terminal_id = t.terminal_id \r\n"
			+ "  where "
			+ "		ah.company_id = ? "
			+ "	 	and ah.client_id = (select c.client_id from sgsapi.client c \r\n"
			+ "				           where c.document = ?) \r\n"
			+ "and ah.status = 'ATTENDED' \r\n"
			+ "order by ah.dt_created asc ", nativeQuery = true)
	List<Object[]> getAllAttendenceHistByDocumentClient(String string, String document);
}

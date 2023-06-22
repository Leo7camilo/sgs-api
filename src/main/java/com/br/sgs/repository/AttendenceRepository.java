package com.br.sgs.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.br.sgs.dtos.AttendenceGroupDateDto;
import com.br.sgs.enums.AttendenceState;
import com.br.sgs.models.AttendenceModel;

public interface AttendenceRepository
		extends JpaRepository<AttendenceModel, UUID>, JpaSpecificationExecutor<AttendenceModel> {
	
	//Optional<AttendenceModel> findByQueueId(UUID idQueue);

	//List<AttendenceModel> findByClientClientIdAndQueueIdNotOrderByAttendenceId(UUID idClient, UUID idQueue);

	@Query(value="select max(password) FROM attendence att WHERE att.company_id =:companyId and att.dt_created between :before and :after", 
			nativeQuery = true)
	Integer findMaxPasswordByCompanyCompanyIdAndDtCreatedBetween(@Param("companyId")String companyId, 
				@Param("before")LocalDateTime before, 
				@Param("after")LocalDateTime after);

	Optional<AttendenceModel> findByAttendenceIdAndCompanyCompanyId(UUID attendenceId, UUID companyId);

	Optional<AttendenceModel> findByPasswordAndCompanyCompanyId(Integer passaword, UUID companyId);

	Optional<AttendenceModel> findByAttendenceId(UUID attendenceId);

	List<AttendenceModel> findByClientClientIdAndCompanyCompanyId(UUID clientId, UUID companyId);

	List<AttendenceModel> findByClientClientIdAndQueueQueueIdNotOrderByAttendenceId(UUID idClient, UUID idQueue);

	Optional<AttendenceModel> findByQueueQueueId(UUID idQueue);

	Long countByCompanyCompanyIdAndStatusNotIn(UUID companyId, Set<AttendenceState> status);

	Long countByCompanyCompanyIdAndDtCreatedBetween(UUID companyId, LocalDateTime ini, LocalDateTime end);

	
	@Query(value = 
			"    SELECT q.description FROM ATTENDENCE att  \r\n"
			+ "  JOIN QUEUE q on att.queue_id = q.queue_id \r\n"
			+ "  WHERE att.company_id =:companyId \r\n"
			+ "	 GROUP BY q.description \r\n"
			+ "  ORDER BY COUNT(q.description) desc", nativeQuery = true)
	List<String> findMostFrequentAttendenceQueue(@Param("companyId")String companyId);
	
	@Query(value = 
			" SELECT q.description, COUNT(*) AS count, DATE_FORMAT(dt_created, '%Y-%m-%d %H:00:00') AS hour \r\n"
			+ "FROM attendence att \r\n"
			+ "join queue q on att.queue_id = q.queue_Id \r\n"
			+ "WHERE att.company_id = :companyId \r\n"
			+ "and DATE(att.dt_created) = :hour \r\n"
			+ "GROUP BY hour, q.description", nativeQuery = true)
	List<Object[]> groupAttendenceByCompanyAndDate(@Param("companyId")String companyId, @Param("hour")LocalDate hour);

}

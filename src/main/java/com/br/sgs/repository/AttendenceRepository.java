package com.br.sgs.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.AttendenceModel;

@Repository
public interface AttendenceRepository
		extends JpaRepository<AttendenceModel, UUID>, JpaSpecificationExecutor<AttendenceModel> {
	
	Optional<AttendenceModel> findByIdQueue(UUID idQueue);

	List<AttendenceModel> findByClientClientIdOrderByAttendenceId(UUID idClient);

	
	@Query(value="select max(password) FROM attendence att WHERE att.company_id =:companyId and att.dt_created between :before and :after", 
			nativeQuery = true)
	Integer findMaxPasswordByCompanyCompanyIdAndDtCreatedBetween(@Param("companyId")String companyId, 
				@Param("before")LocalDateTime before, 
				@Param("after")LocalDateTime after);

	Optional<AttendenceModel> findByAttendenceIdAndCompanyCompanyId(UUID attendenceId, UUID companyId);

	Optional<AttendenceModel> findByPasswordAndCompanyCompanyId(Integer passaword, UUID companyId);

}

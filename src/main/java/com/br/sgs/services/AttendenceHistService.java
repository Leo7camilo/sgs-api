package com.br.sgs.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.br.sgs.dtos.ResponseClientHistDto;
import com.br.sgs.dtos.ResponseCompanyClientDto;
import com.br.sgs.models.AttendenceHistModel;
import com.br.sgs.models.AttendenceModel;

public interface AttendenceHistService {

	Page<AttendenceHistModel> getAllAttendence(Specification<AttendenceHistModel> spec, Pageable pageable);

	void save(AttendenceModel attendenceModel);

	Long countByCompanyCompanyIdAndDtCreatedBetween(UUID companyId, LocalDateTime atStartOfDay,
			LocalDateTime atStartOfDay2);

	ResponseCompanyClientDto mostCompanyClientByCompanyId(UUID companyId);

	List<ResponseClientHistDto> getAllAttendenceHistByDocumentClient(UUID companyId, String document);
}

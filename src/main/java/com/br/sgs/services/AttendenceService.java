package com.br.sgs.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.br.sgs.dtos.AttendenceDto;
import com.br.sgs.models.AttendenceModel;
import com.br.sgs.models.ClientModel;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.TerminalModel;
import com.br.sgs.specifications.SpecificationTemplate.AttendenceSpec;

public interface AttendenceService {

	Page<AttendenceModel> getAllAttendence(AttendenceSpec spec, Pageable pageable);

	void save(AttendenceDto attendenceDto, CompanyModel company, ClientModel clientModel);

	void updateStatus(AttendenceDto attendenceDto, UUID idCompany, UUID idQueue, TerminalModel terminal);

	Optional<AttendenceModel> findByIdQueue(UUID idQueue);

	void updateStatus(UUID idCompany, UUID idQueue, UUID idClient);

	Integer getNextPassword(UUID companyId);

	Page<AttendenceModel>  getAllAttendence(Specification<AttendenceModel> spec, Pageable pageable);

	void save(AttendenceDto attendenceDto, CompanyModel companyModel);

	Optional<AttendenceModel> findByIdAndCompanyId(UUID attendenceId, UUID companyId);

	void updateStatus(UUID companyId, TerminalModel terminal, AttendenceModel attendence);
}

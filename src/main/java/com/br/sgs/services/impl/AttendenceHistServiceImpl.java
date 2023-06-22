package com.br.sgs.services.impl;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.CompanyClientDto;
import com.br.sgs.dtos.ResponseCompanyClientDto;
import com.br.sgs.models.AttendenceHistModel;
import com.br.sgs.models.AttendenceModel;
import com.br.sgs.repository.AttendenceHistRepository;
import com.br.sgs.services.AttendenceHistService;

@Service
public class AttendenceHistServiceImpl implements AttendenceHistService {

	@Autowired
	AttendenceHistRepository attendenceHistRepository;


	@Override
	public Page<AttendenceHistModel> getAllAttendence(Specification<AttendenceHistModel> spec, Pageable pageable) {
		return attendenceHistRepository.findAll(spec, pageable);
	}

	@Override
	public void save(AttendenceModel attendenceModel) {
		AttendenceHistModel attendenceHistModel = new AttendenceHistModel();
		BeanUtils.copyProperties(attendenceModel, attendenceHistModel);
		attendenceHistRepository.save(attendenceHistModel);
	}

	@Override
	public Long countByCompanyCompanyIdAndDtCreatedBetween(UUID companyId, LocalDateTime atStartOfDay,
			LocalDateTime atStartOfDay2) {
		return attendenceHistRepository.countByCompanyCompanyIdAndDtCreatedBetween(companyId, atStartOfDay, atStartOfDay2);
	}
	
	
	@Override
	public ResponseCompanyClientDto mostCompanyClientByCompanyId(UUID companyId) {
		
		List<CompanyClientDto> listCompanyClientDto = new ArrayList<>();
		ResponseCompanyClientDto responseCompanyClientDto = new ResponseCompanyClientDto();
		
		List<Object[]> listObj = attendenceHistRepository.mostCompanyClientByCompanyId(companyId);
		if(listObj != null && !listObj.isEmpty()){
			listObj.forEach(companyClientDtoResult -> {
				CompanyClientDto companyClientDto = new CompanyClientDto();
				
				companyClientDto.setCompany(companyClientDtoResult[0].toString());
				companyClientDto.setCount((BigInteger)companyClientDtoResult[1]);
				final Long totalAttendence = (Long)companyClientDtoResult[2];	
				
				listCompanyClientDto.add(companyClientDto);
				responseCompanyClientDto.setTotalAttendence(totalAttendence);
			});
		}
		
		responseCompanyClientDto.setCompanyClientDto(listCompanyClientDto);
		return responseCompanyClientDto;
	}

}

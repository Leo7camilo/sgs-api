package com.br.sgs.services.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.ClientHistDto;
import com.br.sgs.dtos.CompanyClientDto;
import com.br.sgs.dtos.ResponseClientHistDto;
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
		
		List<Object[]> listObj = attendenceHistRepository.mostCompanyClientByCompanyId(companyId.toString());
		if(listObj != null && !listObj.isEmpty()){
			listObj.forEach(companyClientDtoResult -> {
				CompanyClientDto companyClientDto = new CompanyClientDto();
				
				companyClientDto.setCompany(companyClientDtoResult[0].toString());
				companyClientDto.setCount(((BigInteger)companyClientDtoResult[1]));
				final BigInteger totalAttendence = (BigInteger)companyClientDtoResult[2];
				BigInteger hundred = new BigInteger("100");
				BigDecimal percent = new BigDecimal(companyClientDto.getCount().multiply(hundred).divide(totalAttendence));
				percent.setScale(2);
				
				companyClientDto.setDescription("Representando "+percent+"% do total de atendimentos");
				
				listCompanyClientDto.add(companyClientDto);
				responseCompanyClientDto.setTotalAttendence(totalAttendence);
			});
		}
		
		responseCompanyClientDto.setCompanyClientDto(listCompanyClientDto);
		return responseCompanyClientDto;
	}
	
	/*

	@Override
	public List<ResponseClientHistDto> getAllAttendenceHistByDocumentClient(UUID companyId, String document) {
		
		List<ResponseClientHistDto> responseResponseClientHistDtoList = new ArrayList<>();
		
		List<ClientHistDto> listClientHistDto = new ArrayList<>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		
		LocalDate entryTimePrevious = null;
		List<Object[]> listObj = attendenceHistRepository.getAllAttendenceHistByDocumentClient(companyId.toString(), document);
		if(listObj != null && !listObj.isEmpty()){
			
		LocalDate entryTime = null;
		ResponseClientHistDto responseResponseClientHistDto = new ResponseClientHistDto();	
			for(Object[] clientHistDtoQuery:  listObj) {
				
				ClientHistDto clientHistDto = new ClientHistDto();
				
				if(clientHistDtoQuery[1].toString().equals("ATENDIMENTO")) {
					clientHistDto.setName(clientHistDtoQuery[0].toString());
					clientHistDto.setQueueDescription(clientHistDtoQuery[1].toString());
					clientHistDto.setEntryTime(LocalDateTime.parse(clientHistDtoQuery[2].toString(), formatter));
					clientHistDto.setExitTime(LocalDateTime.parse(clientHistDtoQuery[3].toString(), formatter));
					clientHistDto.setTerminalDescription(clientHistDtoQuery[4].toString());
					responseResponseClientHistDto.setParenteAttendence(clientHistDto);
					entryTimePrevious = LocalDateTime.parse(clientHistDtoQuery[2].toString(), formatter).toLocalDate();
					
				}else {
					clientHistDto.setName(clientHistDtoQuery[0].toString());
					clientHistDto.setQueueDescription(clientHistDtoQuery[1].toString());
					clientHistDto.setEntryTime(LocalDateTime.parse(clientHistDtoQuery[2].toString(), formatter));
					clientHistDto.setExitTime(LocalDateTime.parse(clientHistDtoQuery[3].toString(), formatter));
					clientHistDto.setTerminalDescription(clientHistDtoQuery[4].toString());
					listClientHistDto.add(clientHistDto);	
					responseResponseClientHistDto.setChildAttendence(listClientHistDto);
					entryTime = LocalDateTime.parse(clientHistDtoQuery[2].toString(), formatter).toLocalDate();

				}

				System.out.println(entryTime);
				System.out.println(entryTimePrevious);
				if(entryTime != null && !entryTime.equals(entryTimePrevious)) {
					System.out.println("Entrei no if");
					responseResponseClientHistDtoList.add(responseResponseClientHistDto);
					responseResponseClientHistDto = new ResponseClientHistDto(); 
					listClientHistDto = new ArrayList<>();
				}else {
					System.out.println("Entrei no else");
				}
				
			}
			responseResponseClientHistDtoList.add(responseResponseClientHistDto);
		}
	
		return responseResponseClientHistDtoList;
	}*/
	
	@Override
	public List<ResponseClientHistDto> getAllAttendenceHistByDocumentClient(UUID companyId, String document) {
	    List<ResponseClientHistDto> responseResponseClientHistDtoList = new ArrayList<>();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	    List<Object[]> listObj = attendenceHistRepository.getAllAttendenceHistByDocumentClient(companyId.toString(), document);

	    if (listObj != null && !listObj.isEmpty()) {
	        ResponseClientHistDto responseResponseClientHistDto = null;
	        ClientHistDto parentAttendance = null;
	        List<ClientHistDto> childAttendances = new ArrayList<>();
	        LocalDate entryTimePrevious = null;

	        for (Object[] clientHistDtoQuery : listObj) {
	            String name = clientHistDtoQuery[0].toString();
	            String queueDescription = clientHistDtoQuery[1].toString();
	            LocalDateTime entryTime = LocalDateTime.parse(clientHistDtoQuery[2].toString(), formatter);
	            LocalDateTime exitTime = LocalDateTime.parse(clientHistDtoQuery[3].toString(), formatter);
	            String terminalDescription = clientHistDtoQuery[4].toString();

	            if (queueDescription.equals("ATENDIMENTO")) {
	                if (responseResponseClientHistDto != null) {
	                    responseResponseClientHistDto.setChildAttendence(childAttendances);
	                    responseResponseClientHistDtoList.add(responseResponseClientHistDto);
	                }

	                // Create a new parent attendance
	                parentAttendance = new ClientHistDto();
	                parentAttendance.setName(name);
	                parentAttendance.setQueueDescription(queueDescription);
	                parentAttendance.setEntryTime(entryTime);
	                parentAttendance.setExitTime(exitTime);
	                parentAttendance.setTerminalDescription(terminalDescription);

	                // Create a new response DTO
	                responseResponseClientHistDto = new ResponseClientHistDto();
	                responseResponseClientHistDto.setParenteAttendence(parentAttendance);
	                childAttendances = new ArrayList<>();

	                entryTimePrevious = entryTime.toLocalDate();
	            } else {
	                // Create a new child attendance
	                ClientHistDto childAttendance = new ClientHistDto();
	                childAttendance.setName(name);
	                childAttendance.setQueueDescription(queueDescription);
	                childAttendance.setEntryTime(entryTime);
	                childAttendance.setExitTime(exitTime);
	                childAttendance.setTerminalDescription(terminalDescription);

	                childAttendances.add(childAttendance);
	            }
	        }

	        if (responseResponseClientHistDto != null) {
	            responseResponseClientHistDto.setChildAttendence(childAttendances);
	            responseResponseClientHistDtoList.add(responseResponseClientHistDto);
	        }
	    }

	    return responseResponseClientHistDtoList;
	}



}

package com.br.sgs.services.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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


}

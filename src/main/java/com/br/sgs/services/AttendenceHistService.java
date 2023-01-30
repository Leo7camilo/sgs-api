package com.br.sgs.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.br.sgs.models.AttendenceHistModel;
import com.br.sgs.models.AttendenceModel;

public interface AttendenceHistService {

	Page<AttendenceHistModel> getAllAttendence(Specification<AttendenceHistModel> spec, Pageable pageable);

	void save(AttendenceModel attendenceModel);
}

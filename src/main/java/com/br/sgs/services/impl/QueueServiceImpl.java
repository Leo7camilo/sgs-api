package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.QueueDto;
import com.br.sgs.enums.QueueState;
import com.br.sgs.models.CompanyModel;
import com.br.sgs.models.QueueHistModel;
import com.br.sgs.models.QueueModel;
import com.br.sgs.models.RoleQueueModel;
import com.br.sgs.models.UserModel;
import com.br.sgs.repository.QueueHistRepository;
import com.br.sgs.repository.QueueRepository;
import com.br.sgs.repository.RoleQueueRepository;
import com.br.sgs.services.CompanyService;
import com.br.sgs.services.QueueService;
import com.br.sgs.services.UserService;
import com.br.sgs.specifications.SpecificationTemplate.QueueHistSpec;


@Service
public class QueueServiceImpl implements QueueService {

	@Autowired
	CompanyService companyService;
	
	@Autowired
	QueueRepository queueRepository;
	
	@Autowired
	RoleQueueRepository roleQueueRepository;
	
	@Autowired
	QueueHistRepository queueHistRepository;
	
	@Autowired
	UserService userService;
	
	@Override
	public boolean existsByDescription(String description) {
		return queueRepository.existsByDescription(description);
	}

	@Override
	@Transactional
	public QueueModel save(QueueDto queueDto, UUID idCompany) {
		
		Optional<CompanyModel> company = companyService.findById(idCompany);
		
		var queueModel = new QueueModel();
		BeanUtils.copyProperties(queueDto, queueModel);
		queueModel.setStatus(QueueState.ACTIVE);
		queueModel.setCompany(company.get());
		
		queueModel = queueRepository.save(queueModel);
		
		for(UUID idRole: queueDto.getIdRoles()) {
			var roleQueueModel = new RoleQueueModel();
			roleQueueModel.setIdQueue(queueModel.getQueueId());
			roleQueueModel.setIdRole(idRole);
			
			roleQueueRepository.save(roleQueueModel);
		}
		
		return queueModel;
	}

	@Override
	public Optional<QueueModel> findById(UUID idQueue) {
		return queueRepository.findById(idQueue);
	}
	

	@Override
	@Transactional
	public QueueModel updateStatus(QueueModel queueModel, UUID idCompany, UserDetails userDetails) {
		
		if(queueModel.getStatus() == QueueState.ACTIVE)
			queueModel.setStatus(QueueState.DISABLE);
		else
			queueModel.setStatus(QueueState.ACTIVE);
		
		Optional<UserModel> userModelOptional = userService.findByUsername(userDetails.getUsername());
		Optional<QueueModel> queueModelOptional = queueRepository.findById(queueModel.getQueueId());
		
		
		queueModel.setQueueId(queueModel.getQueueId());
		
		QueueHistModel queueHistModel = new QueueHistModel();
		queueHistModel.setDescription(queueModel.getDescription());
		queueHistModel.setQueue(queueModelOptional.get());
		queueHistModel.setUser(userModelOptional.get());
		queueHistModel.setTsChange(LocalDateTime.now(ZoneId.of("UTC")));

		queueHistRepository.save(queueHistModel);
		return queueRepository.save(queueModel);
	}

	@Override
	public boolean existsById(UUID id) {
		return queueRepository.existsById(id);
	}


	@Override
	@Transactional
	public QueueModel update(QueueDto queueDto, UUID idCompany, UUID idQueue) {
		
		Optional<CompanyModel> company = companyService.findById(idCompany);
		
		var queueModel = new QueueModel();
		BeanUtils.copyProperties(queueDto, queueModel);
		queueModel.setCompany(company.get());
		queueModel.setQueueId(idQueue);
		return queueRepository.save(queueModel);
	}

	@Override
	public List<QueueModel> orderListQueueByPriority(List<UUID> idQueueList) {
		return queueRepository.findAllByQueueIdInOrderByPriority(idQueueList);
	}

	@Override
	public Optional<QueueModel> findByIdAndCompanyId(UUID queueId, UUID companyId) {
		return queueRepository.findByQueueIdAndCompanyCompanyId(queueId, companyId);
	}

	@Override
	public Page<QueueModel> findAllByCompany(Specification<QueueModel> spec, Pageable pageable) {
		return queueRepository.findAll(spec, pageable);
	}

	@Override
	public Page<QueueHistModel> findAllHistByCompany(Specification<QueueHistModel> spec, Pageable pageable) {
		return queueHistRepository.findAll(spec, pageable);
	}

	@Override
	public List<QueueModel> findByCompanyId(UUID companyId) {
		return queueRepository.findAllByCompanyCompanyId(companyId);
	}

	@Override
	public Page<QueueHistModel> findAllHistByQueueIn(Specification<QueueHistModel> spec, Pageable pageable) {
		return queueHistRepository.findAll(spec, pageable);
	}	

	
	
}

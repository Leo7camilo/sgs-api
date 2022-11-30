package com.br.sgs.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.br.sgs.dtos.QueueDto;
import com.br.sgs.enums.QueueState;
import com.br.sgs.models.QueueHistModel;
import com.br.sgs.models.QueueModel;
import com.br.sgs.models.RoleQueueModel;
import com.br.sgs.models.UserModel;
import com.br.sgs.repository.QueueHistRepository;
import com.br.sgs.repository.QueueRepository;
import com.br.sgs.repository.RoleQueueRepository;
import com.br.sgs.services.QueueService;


@Service
public class QueueServiceImpl implements QueueService {

	
	@Autowired
	QueueRepository queueRepository;
	
	@Autowired
	RoleQueueRepository roleQueueRepository;
	
	@Autowired
	QueueHistRepository queueHistRepository;
	
	@Override
	public boolean existsByDescription(String description) {
		return queueRepository.existsByDescription(description);
	}

	@Override
	@Transactional
	public QueueModel save(QueueDto queueDto, UUID idCompany) {
		
		var queueModel = new QueueModel();
		BeanUtils.copyProperties(queueDto, queueModel);
		queueModel.setStatus(QueueState.ACTIVE);
		queueModel.setIdCompany(idCompany);
		
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
	public QueueModel updateStatus(QueueModel queueModel, UUID idCompany) {
		
		if(queueModel.getStatus() == QueueState.ACTIVE)
			queueModel.setStatus(QueueState.DISABLE);
		else
			queueModel.setStatus(QueueState.ACTIVE);
		
		queueModel.setQueueId(queueModel.getQueueId());
		
		QueueHistModel queueHistModel = new QueueHistModel();
		queueHistModel.setDescription(queueModel.getDescription());
		queueHistModel.setIdQueue(queueModel.getQueueId());
		queueHistModel.setIdUser(new UserModel());
		queueHistModel.setTsChange(LocalDateTime.now(ZoneId.of("UTC")));

		queueHistRepository.save(queueHistModel);
		return queueRepository.save(queueModel);
	}

	@Override
	public boolean existsById(UUID id) {
		return queueRepository.existsById(id);
	}


	@Override
	public QueueModel update(QueueDto queueDto, UUID idCompany, UUID idQueue) {
		var queueModel = new QueueModel();
		BeanUtils.copyProperties(queueDto, queueModel);
		queueModel.setIdCompany(idCompany);
		queueModel.setQueueId(idQueue);
		return queueRepository.save(queueModel);
	}

	@Override
	public List<QueueModel> orderListQueueByPriority(List<UUID> idQueueList) {
		return queueRepository.findAllByIdOrderById(idQueueList);
	}

}

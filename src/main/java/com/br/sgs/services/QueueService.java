package com.br.sgs.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetails;

import com.br.sgs.dtos.QueueDto;
import com.br.sgs.dtos.QueueDto.QueueView.QueuePut;
import com.br.sgs.dtos.QueueDto.QueueView.RegistrationPost;
import com.br.sgs.models.QueueHistModel;
import com.br.sgs.models.QueueModel;

public interface QueueService {

	boolean existsByDescription(String description, UUID companyId);

	QueueModel save(QueueDto queueDto, UUID idCompany);

	Optional<QueueModel> findById(UUID idQueue);

	QueueModel updateStatus(QueueModel queueModel, UUID idCompany, UserDetails userDetails);

	boolean existsById(UUID id);

	QueueModel update(QueueDto queueDto, UUID idCompany, UUID idQueue);

	List<QueueModel> orderListQueueByPriority(List<UUID> idQueueList);

	Optional<QueueModel> findByIdAndCompanyId(UUID queueId, UUID companyId);

	Page<QueueModel> findAllByCompany(Specification<QueueModel> spec, Pageable pageable);

	Page<QueueHistModel> findAllHistByCompany(Specification<QueueHistModel> spec, Pageable pageable);

	List<QueueModel> findByCompanyId(UUID companyId);

	Page<QueueHistModel> findAllHistByQueueIn(Specification<QueueHistModel> spec, Pageable pageable);

	boolean existsByPriority(Integer priority, UUID companyId);

	Optional<QueueModel> findByCompanyIdAndDescription(UUID companyId, String description);
}

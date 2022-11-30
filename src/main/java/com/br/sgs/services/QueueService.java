package com.br.sgs.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.br.sgs.dtos.QueueDto;
import com.br.sgs.models.QueueModel;

public interface QueueService {

	boolean existsByDescription(String description);

	QueueModel save(QueueDto queueDto, UUID idCompany);

	Optional<QueueModel> findById(UUID idQueue);

	QueueModel updateStatus(QueueModel queueModel, UUID idCompany);

	boolean existsById(UUID id);

	QueueModel update(QueueDto queueDto, UUID idCompany, UUID idQueue);

	List<QueueModel> orderListQueueByPriority(List<UUID> idQueueList);
}

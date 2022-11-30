package com.br.sgs.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.QueueModel;

@Repository
public interface QueueRepository extends JpaRepository<QueueModel, UUID>{

	boolean existsByDescription(String description);

	List<QueueModel> findAllByIdOrderById(List<UUID> idQueueList);

}

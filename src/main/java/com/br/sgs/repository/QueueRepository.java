package com.br.sgs.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.QueueModel;

@Repository
public interface QueueRepository extends JpaRepository<QueueModel, UUID>, JpaSpecificationExecutor<QueueModel> {

	List<QueueModel> findAllByQueueIdInOrderByQueueId(List<UUID> idQueueList);

	Optional<QueueModel> findByQueueIdAndCompanyCompanyId(UUID queueId, UUID companyId);

	List<QueueModel> findAllByCompanyCompanyId(UUID companyId);

	List<QueueModel> findAllByQueueIdInOrderByPriority(List<UUID> idQueueList);

	boolean existsByDescriptionAndCompanyCompanyId(String description, UUID companyId);

	boolean existsByPriorityAndCompanyCompanyId(Integer priority, UUID companyId);

}

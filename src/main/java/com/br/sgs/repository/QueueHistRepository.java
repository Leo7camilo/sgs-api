package com.br.sgs.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.QueueHistModel;

@Repository
public interface QueueHistRepository extends JpaRepository<QueueHistModel, UUID>, JpaSpecificationExecutor<QueueHistModel> {

}

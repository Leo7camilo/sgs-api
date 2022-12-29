package com.br.sgs.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.br.sgs.models.RoleModel;
import com.br.sgs.models.TerminalModel;


@Repository
public interface RoleRepository extends JpaRepository<RoleModel, UUID>, JpaSpecificationExecutor<RoleModel> {
	
	boolean existsByDescription(String description);
	
	boolean existsById(UUID id);

	Optional<RoleModel> findByRoleIdAndCompanyCompanyId(UUID roleId, UUID companyId);

}

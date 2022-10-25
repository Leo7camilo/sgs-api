package com.br.sgs.services;

import java.util.Optional;
import java.util.UUID;

import com.br.sgs.dtos.RoleDto;
import com.br.sgs.models.RoleModel;

public interface RoleService {

	boolean existsByDescription(String descriptions);

	RoleModel save(RoleDto roleDto, UUID idCompany);

	boolean existsById(UUID idRole);

	Optional<RoleModel> findById(UUID idRole);

	RoleModel update(RoleModel RoleModel, UUID idCompany);

}

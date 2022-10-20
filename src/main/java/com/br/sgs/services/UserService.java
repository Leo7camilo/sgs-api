package com.br.sgs.services;

import com.br.sgs.dtos.UserDto;
import com.br.sgs.models.UserModel;

public interface UserService {

	UserModel save(UserDto userDto);

	boolean existsByEmail(String email);


}

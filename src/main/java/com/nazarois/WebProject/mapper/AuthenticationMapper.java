package com.nazarois.WebProject.mapper;

import com.nazarois.WebProject.dto.authentication.AuthenticateDto;
import com.nazarois.WebProject.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthenticationMapper {
  User RegisterDtoToUser(AuthenticateDto authenticateDto);
}

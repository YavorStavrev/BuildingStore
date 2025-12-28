package com.tu.javabuildingstore.mapper;

import com.tu.javabuildingstore.dto.user.RegisterRequestDTO;
import com.tu.javabuildingstore.dto.user.RegisterResponseDTO;
import com.tu.javabuildingstore.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = PasswordEncoderMapper.class)
public interface UserMapper {

    @Mapping(target = "password", source = "password", qualifiedByName = "encode")
    User toEntity(RegisterRequestDTO dto);

    RegisterResponseDTO toResponse(User user);

    default RegisterResponseDTO toResponse(User user, String message) {
        if (user == null)
            return new RegisterResponseDTO(
                    null,       // id
                    null,       // username
                    null,       // email
                    message,    // message
                    null        // token
            );
        return new RegisterResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                message,
                null
        );
    }
}


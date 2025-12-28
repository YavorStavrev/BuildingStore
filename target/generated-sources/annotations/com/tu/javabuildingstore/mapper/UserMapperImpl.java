package com.tu.javabuildingstore.mapper;

import com.tu.javabuildingstore.dto.user.RegisterRequestDTO;
import com.tu.javabuildingstore.dto.user.RegisterResponseDTO;
import com.tu.javabuildingstore.model.User;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-12-28T14:32:02+0200",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.9 (Microsoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private PasswordEncoderMapper passwordEncoderMapper;

    @Override
    public User toEntity(RegisterRequestDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.password( passwordEncoderMapper.encode( dto.password() ) );
        user.username( dto.username() );
        user.email( dto.email() );
        user.firstName( dto.firstName() );
        user.lastName( dto.lastName() );

        return user.build();
    }

    @Override
    public RegisterResponseDTO toResponse(User user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String username = null;
        String email = null;

        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();

        String message = null;
        String token = null;

        RegisterResponseDTO registerResponseDTO = new RegisterResponseDTO( id, username, email, message, token );

        return registerResponseDTO;
    }
}

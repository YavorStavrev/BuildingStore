package com.tu.javabuildingstore.mapper;

import org.mapstruct.Named;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderMapper {
    private final PasswordEncoder passwordEncoder;

    public PasswordEncoderMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Named("encode")
    public String encode(String raw) {
        if (raw == null) return null;
        return passwordEncoder.encode(raw);
    }
}

package com.tu.javabuildingstore.service;

import com.tu.javabuildingstore.dto.role.RoleRequestDTO;
import com.tu.javabuildingstore.model.Role;
import com.tu.javabuildingstore.model.User;
import com.tu.javabuildingstore.repository.RoleRepository;
import com.tu.javabuildingstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public Role createRole(RoleRequestDTO dto) {
        if (roleRepository.existsByName(dto.name())) {
            throw new IllegalArgumentException("Role already exists: " + dto.name());
        }
        Role role = Role.builder()
                .name(dto.name())
                .build();
        return roleRepository.save(role);
    }

    @Transactional
    public void assignRoleToUser(String username, String roleName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalArgumentException("Role not found: " + roleName));

        if (user.getRoles() == null) {
            user.setRoles(new ArrayList<>());
        }
        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
        }

        if (role.getUsers() == null) {
            role.setUsers(new ArrayList<>());
        }
        if (!role.getUsers().contains(user)) {
            role.getUsers().add(user);
        }

        userRepository.save(user);
    }
}

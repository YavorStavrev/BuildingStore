package com.tu.javabuildingstore.service;

import com.tu.javabuildingstore.model.Role;
import com.tu.javabuildingstore.model.User;
import com.tu.javabuildingstore.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        // Arrange
        Role roleUser = new Role();
        roleUser.setName("ROLE_USER");

        User user = User.builder()
                .username("john")
                .password("encodedPass")
                .roles(List.of(roleUser))
                .build();

        when(userRepository.findByUsername("john"))
                .thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userService.loadUserByUsername("john");

        // Assert
        assertNotNull(userDetails);
        assertEquals("john", userDetails.getUsername());
        assertEquals("encodedPass", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(
                userDetails.getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_USER"))
        );
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(
                UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("missing")
        );
    }
}
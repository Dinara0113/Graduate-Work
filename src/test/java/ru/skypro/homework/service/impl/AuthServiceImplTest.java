package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.Role;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    private UserDetailsManager userDetailsManager;
    private PasswordEncoder passwordEncoder;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        userDetailsManager = mock(UserDetailsManager.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authService = new AuthServiceImpl(userDetailsManager, passwordEncoder);
    }

    @Test
    void login_shouldReturnTrue_whenCredentialsAreValid() {
        String username = "user";
        String rawPassword = "pass";
        String encodedPassword = "encodedPass";

        UserDetails userDetails = User.withUsername(username)
                .password(encodedPassword)
                .roles("USER")
                .build();

        when(userDetailsManager.userExists(username)).thenReturn(true);
        when(userDetailsManager.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(rawPassword, encodedPassword)).thenReturn(true);

        boolean result = authService.login(username, rawPassword);

        assertTrue(result);
    }

    @Test
    void login_shouldReturnFalse_whenUserNotFound() {
        when(userDetailsManager.userExists("ghost")).thenReturn(false);

        boolean result = authService.login("ghost", "any");

        assertFalse(result);
    }

    @Test
    void login_shouldReturnFalse_whenPasswordInvalid() {
        String username = "user";
        String wrongPassword = "wrong";
        String encodedPassword = "encoded";

        UserDetails userDetails = User.withUsername(username)
                .password(encodedPassword)
                .roles("USER")
                .build();

        when(userDetailsManager.userExists(username)).thenReturn(true);
        when(userDetailsManager.loadUserByUsername(username)).thenReturn(userDetails);
        when(passwordEncoder.matches(wrongPassword, encodedPassword)).thenReturn(false);

        boolean result = authService.login(username, wrongPassword);

        assertFalse(result);
    }

    @Test
    void register_shouldCreateUser_whenNotExists() {
        Register register = new Register();
        register.setUsername("newUser");
        register.setPassword("pass12345");
        register.setRole(Role.USER);
        register.setFirstName("Test");
        register.setLastName("User");
        register.setPhone("+7 (999) 123-45-67");

        when(userDetailsManager.userExists("newUser")).thenReturn(false);

        boolean result = authService.register(register);

        assertTrue(result);
        verify(userDetailsManager).createUser(argThat(user ->
                user.getUsername().equals("newUser") &&
                        user.getAuthorities().stream().anyMatch(
                                auth -> auth.getAuthority().equals("ROLE_USER")
                        )
        ));
    }

    @Test
    void register_shouldReturnFalse_whenUserExists() {
        Register register = new Register();
        register.setUsername("existing");
        register.setPassword("pass12345");
        register.setRole(Role.USER);
        register.setFirstName("Existing");
        register.setLastName("User");
        register.setPhone("+7 (888) 123-45-67");

        when(userDetailsManager.userExists("existing")).thenReturn(true);

        boolean result = authService.register(register);

        assertFalse(result);
        verify(userDetailsManager, never()).createUser(any());
    }
}

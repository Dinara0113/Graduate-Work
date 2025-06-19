package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {

    private AuthServiceImpl authService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authService = new AuthServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void login_shouldReturnTrue_whenUserExistsAndPasswordMatches() {
        User user = new User();
        user.setPassword("encodedPass");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPass")).thenReturn(true);

        assertTrue(authService.login("test@mail.com", "password"));
    }

    @Test
    void login_shouldReturnFalse_whenUserNotFound() {
        when(userRepository.findByEmail("ghost@mail.com")).thenReturn(Optional.empty());

        assertFalse(authService.login("ghost@mail.com", "password"));
    }

    @Test
    void login_shouldReturnFalse_whenPasswordDoesNotMatch() {
        User user = new User();
        user.setPassword("encodedPass");

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encodedPass")).thenReturn(false);

        assertFalse(authService.login("test@mail.com", "wrong"));
    }

    @Test
    void register_shouldSaveUser_whenUserNotExists() {
        Register register = new Register();
        register.setUsername("new@mail.com");
        register.setPassword("123456");
        register.setFirstName("First");
        register.setLastName("Last");
        register.setPhone("1234567890");
        register.setRole(Role.USER);

        when(userRepository.findByEmail("new@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("encodedPassword");

        boolean result = authService.register(register);

        assertTrue(result);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());

        User savedUser = captor.getValue();
        assertEquals("new@mail.com", savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals("First", savedUser.getFirstName());
        assertEquals("Last", savedUser.getLastName());
        assertEquals("1234567890", savedUser.getPhone());
        assertEquals(Role.USER, savedUser.getRole());
    }

    @Test
    void register_shouldReturnFalse_whenUserAlreadyExists() {
        Register register = new Register();
        register.setUsername("exists@mail.com");

        when(userRepository.findByEmail("exists@mail.com")).thenReturn(Optional.of(new User()));

        assertFalse(authService.register(register));
        verify(userRepository, never()).save(any());
    }

    @Test
    void changePassword_shouldUpdatePassword_whenOldPasswordMatches() {
        User user = new User();
        user.setEmail("user@mail.com");
        user.setPassword("encodedOld");

        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("old", "encodedOld")).thenReturn(true);
        when(passwordEncoder.encode("new")).thenReturn("encodedNew");

        Optional<NewPassword> result = authService.changePassword("user@mail.com", "old", "new");

        assertTrue(result.isPresent());
        assertEquals("******", result.get().getNewPassword());

        verify(userRepository).save(user);
        assertEquals("encodedNew", user.getPassword());
    }

    @Test
    void changePassword_shouldReturnEmpty_whenUserNotFound() {
        when(userRepository.findByEmail("ghost@mail.com")).thenReturn(Optional.empty());

        Optional<NewPassword> result = authService.changePassword("ghost@mail.com", "old", "new");

        assertTrue(result.isEmpty());
    }

    @Test
    void changePassword_shouldReturnEmpty_whenOldPasswordDoesNotMatch() {
        User user = new User();
        user.setPassword("encodedOld");

        when(userRepository.findByEmail("user@mail.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongOld", "encodedOld")).thenReturn(false);

        Optional<NewPassword> result = authService.changePassword("user@mail.com", "wrongOld", "new");

        assertTrue(result.isEmpty());
        verify(userRepository, never()).save(any());
    }
}

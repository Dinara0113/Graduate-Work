package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.skypro.homework.dto.CreateUser;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.service.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserRepository userRepository;
    private UserMapper userMapper;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        userService = new UserServiceImpl(userRepository, userMapper);
    }

    @Test
    void getUserById_ReturnsUserDto() {
        User user = new User();
        user.setId(1);
        UserDto dto = new UserDto();
        dto.setId(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        UserDto result = userService.getUserById(1);

        assertThat(result).isEqualTo(dto);
        verify(userRepository).findById(1);
        verify(userMapper).toDto(user);
    }

    @Test
    void getUserById_UserNotFound_ThrowsException() {
        when(userRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(99))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }

    @Test
    void getAllUsers_ReturnsListOfUserDto() {
        User user1 = new User();
        User user2 = new User();
        UserDto dto1 = new UserDto();
        UserDto dto2 = new UserDto();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.toDto(user1)).thenReturn(dto1);
        when(userMapper.toDto(user2)).thenReturn(dto2);

        List<UserDto> result = userService.getAllUsers();

        assertThat(result).containsExactly(dto1, dto2);
        verify(userRepository).findAll();
    }

    @Test
    void createUser_ReturnsCreatedUserDto() {
        CreateUser createUser = new CreateUser();
        User userEntity = new User();
        User savedUser = new User();
        UserDto userDto = new UserDto();

        when(userMapper.toEntity(createUser)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(userDto);

        UserDto result = userService.createUser(createUser);

        assertThat(result).isEqualTo(userDto);
        verify(userRepository).save(userEntity);
        verify(userMapper).toEntity(createUser);
        verify(userMapper).toDto(savedUser);
    }

    @Test
    void updateUser_ReturnsUpdatedUserDto() {
        int userId = 1;

        UpdateUser updateUser = new UpdateUser();
        updateUser.setFirstName("Updated");
        updateUser.setLastName("User");

        User existingUser = new User();
        existingUser.setId(userId);

        User updateEntity = new User();
        updateEntity.setFirstName("Updated");
        updateEntity.setLastName("User");

        User savedUser = new User();
        savedUser.setId(userId);
        savedUser.setFirstName("Updated");
        savedUser.setLastName("User");

        UserDto resultDto = new UserDto();
        resultDto.setId(userId);
        resultDto.setFirstName("Updated");
        resultDto.setLastName("User");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userMapper.toEntity(updateUser)).thenReturn(updateEntity);
        when(userRepository.save(updateEntity)).thenReturn(savedUser);
        when(userMapper.toDto(savedUser)).thenReturn(resultDto);

        UserDto result = userService.updateUser(userId, updateUser);

        assertThat(result).isEqualTo(resultDto);
        verify(userRepository).findById(userId);
        verify(userRepository).save(updateEntity);
        verify(userMapper).toEntity(updateUser);
        verify(userMapper).toDto(savedUser);
    }

    @Test
    void deleteUser_CallsRepository() {
        userService.deleteUser(1);
        verify(userRepository).deleteById(1);
    }
}

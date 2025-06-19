package ru.skypro.homework.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateUser;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.mapper.UserMapper;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.UserService;
import ru.skypro.homework.model.User;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для управления пользователями.
 * Реализует логику CRUD операций и обновления изображения пользователя.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Получить пользователя по ID.
     *
     * @param id ID пользователя
     * @return {@link UserDto}
     */
    @Override
    public UserDto getUserById(Integer id) {
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Получить список всех пользователей.
     *
     * @return список {@link UserDto}
     */
    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Создать нового пользователя.
     *
     * @param user DTO с данными пользователя
     * @return созданный {@link UserDto}
     */
    @Override
    public UserDto createUser(CreateUser user) {
        return userMapper.toDto(
                userRepository.save(userMapper.toEntity(user))
        );
    }

    /**
     * Обновить данные пользователя.
     *
     * @param id    ID пользователя
     * @param user  DTO с обновлёнными данными
     * @return обновлённый {@link UserDto}
     */
    @Override
    public UserDto updateUser(Integer id, UpdateUser user) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User updated = userMapper.toEntity(user);
        updated.setId(existing.getId());
        return userMapper.toDto(userRepository.save(updated));
    }

    /**
     * Удалить пользователя по ID.
     *
     * @param id ID пользователя
     */
    @Override
    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    /**
     * Обновить изображение текущего пользователя.
     *
     * @param image изображение в виде {@link MultipartFile}
     * @throws IOException если не удалось прочитать изображение
     */
    @Override
    public void updateUserImage(MultipartFile image) throws IOException {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        user.setImage(image.getBytes());
        userRepository.save(user);
    }
}

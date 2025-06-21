package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.CreateUser;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserDto getUserById(Integer id);

    List<UserDto> getAllUsers();

    UserDto createUser(CreateUser user);

    UserDto updateUser(Integer id, UpdateUser user);

    void deleteUser(Integer id);
    void updateUserImage(MultipartFile image) throws IOException;

}

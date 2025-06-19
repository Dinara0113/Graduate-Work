package ru.skypro.homework.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;

import java.util.Optional;

/**
 * Реализация сервиса авторизации и регистрации пользователей.
 * Обеспечивает вход, регистрацию и смену пароля.
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    /**
     * Выполняет проверку логина и пароля пользователя.
     *
     * @param userName имя пользователя (email)
     * @param password пароль в открытом виде
     * @return true, если авторизация успешна; false — иначе
     */
    @Override
    public boolean login(String userName, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(userName);
        return optionalUser.isPresent() && encoder.matches(password, optionalUser.get().getPassword());
    }

    /**
     * Регистрирует нового пользователя, если email ещё не занят.
     *
     * @param register DTO с данными пользователя
     * @return true, если регистрация успешна; false — если пользователь уже существует
     */
    @Override
    public boolean register(Register register) {
        if (userRepository.findByEmail(register.getUsername()).isPresent()) {
            return false;
        }

        User newUser = new User();
        newUser.setEmail(register.getUsername());
        newUser.setPassword(encoder.encode(register.getPassword()));
        newUser.setFirstName(register.getFirstName());
        newUser.setLastName(register.getLastName());
        newUser.setPhone(register.getPhone());
        newUser.setRole(register.getRole());

        userRepository.save(newUser);
        return true;
    }

    /**
     * Изменяет пароль пользователя.
     *
     * @param username    имя пользователя (email)
     * @param oldPassword текущий пароль
     * @param newPassword новый пароль
     * @return Optional с зашифрованными данными о пароле, если успешно; иначе — пустой Optional
     */
    @Override
    public Optional<NewPassword> changePassword(String username, String oldPassword, String newPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(username);
        if (optionalUser.isEmpty()) {
            return Optional.empty();
        }

        User user = optionalUser.get();
        if (!encoder.matches(oldPassword, user.getPassword())) {
            return Optional.empty();
        }

        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);

        NewPassword result = new NewPassword();
        result.setCurrentPassword("******");
        result.setNewPassword("******");
        return Optional.of(result);
    }
}

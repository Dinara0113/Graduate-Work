package ru.skypro.homework.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.Login;
import ru.skypro.homework.dto.Register;
import ru.skypro.homework.service.AuthService;

/**
 * Контроллер для аутентификации и регистрации пользователей.
 * Обрабатывает вход в систему и регистрацию нового пользователя.
 */
@CrossOrigin(value = "http://localhost:3000")
@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Эндпоинт для входа пользователя в систему.
     *
     * @param login объект с именем пользователя и паролем
     * @return 200 OK при успешной аутентификации, 401 UNAUTHORIZED при неуспешной
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login login) {
        if (authService.login(login.getUsername(), login.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Эндпоинт для регистрации нового пользователя.
     *
     * @param register объект с данными нового пользователя
     * @return 201 CREATED при успешной регистрации, 400 BAD REQUEST если пользователь уже существует
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Register register) {
        if (authService.register(register)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

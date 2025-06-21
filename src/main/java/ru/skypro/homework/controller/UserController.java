package ru.skypro.homework.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.NewPassword;
import ru.skypro.homework.service.AuthService;
import ru.skypro.homework.service.UserService;

import java.io.IOException;
import java.util.Optional;

/**
 * Контроллер для управления пользователями.
 * Предоставляет функциональность смены пароля и загрузки аватаров.
 */
@RestController
@RequestMapping("/users")
@CrossOrigin("http://localhost:3000")
public class UserController {

    private final AuthService authService;
    private final UserService userService;

    public UserController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * Смена пароля пользователя.
     *
     * @param newPassword     объект с текущим и новым паролем
     * @param authentication  информация об аутентифицированном пользователе
     * @return HTTP 200 если пароль успешно изменён, иначе HTTP 401
     */
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword,
                                         Authentication authentication) {
        Optional<NewPassword> updated = authService.changePassword(
                authentication.getName(),
                newPassword.getCurrentPassword(),
                newPassword.getNewPassword()
        );

        return updated
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(401).body("Wrong password"));
    }

    /**
     * Обновление аватара текущего пользователя.
     *
     * @param image           новое изображение
     * @param authentication  информация об аутентифицированном пользователе
     * @return HTTP 200 в случае успеха
     * @throws IOException если произошла ошибка при обработке файла
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/me/image")
    public ResponseEntity<Void> updateUserImage(@RequestParam MultipartFile image,
                                                Authentication authentication) throws IOException {
        userService.updateUserImage(image);
        return ResponseEntity.ok().build();
    }
}

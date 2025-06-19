package ru.skypro.homework.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

/**
 * Контроллер для получения изображений пользователей и объявлений.
 */
@RestController
@RequestMapping("/images")
public class ImageController {

    private final UserRepository userRepository;
    private final AdRepository adRepository;

    public ImageController(UserRepository userRepository, AdRepository adRepository) {
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    /**
     * Получает изображение пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return байтовый массив изображения с типом {@code image/jpeg}
     */
    @GetMapping(value = "/user/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getUserImage(@PathVariable Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пользователь не найден: id=" + id));
        return ResponseEntity.ok(user.getImage());
    }

    /**
     * Получает изображение объявления по его идентификатору.
     *
     * @param id идентификатор объявления
     * @return байтовый массив изображения с типом {@code image/jpeg}
     */
    @GetMapping(value = "/ad/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getAdImage(@PathVariable Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Объявление не найдено: id=" + id));
        return ResponseEntity.ok(ad.getImage());
    }
}

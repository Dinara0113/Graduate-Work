package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

/**
 * Контроллер для работы с объявлениями.
 * Содержит эндпоинты для CRUD-операций с объявлениями,
 * получения изображений, а также работы с объявлениями текущего пользователя.
 */
@RestController
@RequestMapping("/ads")
@CrossOrigin("http://localhost:3000")
public class AdsController {

    private final AdService adService;

    public AdsController(AdService adService) {
        this.adService = adService;
    }

    /**
     * Получение списка всех объявлений.
     *
     * @return список всех объявлений с обёрткой {@link ResponseWrapper}
     */
    @Operation(summary = "Получение всех объявлений", tags = {"Объявления"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список объявлений получен")
    })
    @GetMapping
    public ResponseEntity<ResponseWrapper<AdsDto>> getAllAds() {
        List<AdsDto> ads = adService.getAllAds();
        ResponseWrapper<AdsDto> response = new ResponseWrapper<>();
        response.setCount(ads.size());
        response.setResults(ads);
        return ResponseEntity.ok(response);
    }

    /**
     * Создание нового объявления.
     *
     * @param ad    данные объявления
     * @param image изображение объявления
     * @return созданное объявление
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<AdsDto> addAd(@RequestPart("properties") CreateAds ad,
                                        @RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(201).body(adService.addAd(ad, image));
    }

    /**
     * Получение расширенной информации об объявлении по ID.
     *
     * @param id ID объявления
     * @return подробная информация об объявлении
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAdById(@PathVariable Integer id) {
        return ResponseEntity.ok(adService.getExtendedAd(id));
    }


    /**
     * Удаление объявления по ID.
     * Доступно владельцу или администратору.
     *
     * @param id ID объявления
     * @return 204 No Content
     */
    @PreAuthorize("hasRole('ADMIN') or @adAccess.checkAdOwner(authentication.name, #id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Integer id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }


    /**
     * Обновление данных объявления.
     * Доступно владельцу или администратору.
     *
     * @param id ID объявления
     * @param ad новые данные
     * @return обновлённое объявление
     */
    @PreAuthorize("hasRole('ADMIN') or @adAccess.checkAdOwner(authentication.name, #id)")
    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAd(@PathVariable Integer id, @RequestBody CreateAds ad) {
        return ResponseEntity.ok(adService.updateAd(id, ad));
    }


    /**
     * Обновление изображения объявления.
     * Доступно владельцу или администратору.
     *
     * @param id    ID объявления
     * @param image новое изображение
     * @return массив байт нового изображения
     */
    @PreAuthorize("hasRole('ADMIN') or @adAccess.checkAdOwner(authentication.name, #id)")
    @PatchMapping("/{id}/image")
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id,
                                              @RequestPart("image") MultipartFile image) {
        byte[] updated = adService.updateImage(id, image);
        return ResponseEntity.ok(updated);
    }


    /**
     * Получение объявлений текущего пользователя.
     *
     * @param principal текущий авторизованный пользователь
     * @return список его объявлений
     */
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    @Operation(summary = "Получение объявлений текущего пользователя")
    public ResponseEntity<ResponseWrapper<AdsDto>> getMyAds(Principal principal) {
        String email = principal.getName();
        List<AdsDto> ads = adService.getAdsByEmail(email);

        ResponseWrapper<AdsDto> response = new ResponseWrapper<>();
        response.setCount(ads.size());
        response.setResults(ads);

        return ResponseEntity.ok(response);
    }


    /**
     * Получение изображения объявления.
     *
     * @param id ID объявления
     * @return байты изображения
     */
    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        byte[] image = adService.getImage(id);
        return ResponseEntity
                .ok()
                .header("Content-Type", "image/*") // универсально
                .body(image);
    }


    /**
     * Обновление изображения объявления (через POST-запрос).
     *
     * @param id    ID объявления
     * @param image новое изображение
     * @return 200 OK, если обновлено
     * @throws IOException ошибка чтения файла
     */
    @PreAuthorize("hasRole('USER')")
    @PostMapping(value = "/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> updateAdImage(@PathVariable Integer id,
                                              @RequestParam MultipartFile image) throws IOException {
        adService.updateAdImage(id, image);
        return ResponseEntity.ok().build();
    }

}

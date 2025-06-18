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

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/ads")
@CrossOrigin("http://localhost:3000")
public class AdsController {

    private final AdService adService;

    public AdsController(AdService adService) {
        this.adService = adService;
    }

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

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<AdsDto> addAd(@RequestPart("properties") CreateAds ad,
                                        @RequestPart("image") MultipartFile image) {
        return ResponseEntity.status(201).body(adService.addAd(ad, image));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExtendedAd> getAdById(@PathVariable Integer id) {
        return ResponseEntity.ok(adService.getExtendedAd(id));
    }

    @PreAuthorize("hasRole('ADMIN') or @adAccess.checkAdOwner(authentication.name, #id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAd(@PathVariable Integer id) {
        adService.deleteAd(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN') or @adAccess.checkAdOwner(authentication.name, #id)")
    @PatchMapping("/{id}")
    public ResponseEntity<AdsDto> updateAd(@PathVariable Integer id, @RequestBody CreateAds ad) {
        return ResponseEntity.ok(adService.updateAd(id, ad));
    }

    @PreAuthorize("hasRole('ADMIN') or @adAccess.checkAdOwner(authentication.name, #id)")
    @PatchMapping("/{id}/image")
    public ResponseEntity<byte[]> updateImage(@PathVariable Integer id,
                                              @RequestPart("image") MultipartFile image) {
        byte[] updated = adService.updateImage(id, image);
        return ResponseEntity.ok(updated);
    }

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

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        byte[] image = adService.getImage(id);
        return ResponseEntity
                .ok()
                .header("Content-Type", "image/*") // универсально
                .body(image);
    }


}

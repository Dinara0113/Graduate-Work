package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.AdsDto;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/ads")
@CrossOrigin("http://localhost:3000")
public class AdsController {

    @GetMapping
    public List<AdsDto> getAllAds() {
        return Collections.emptyList();
    }

    @PostMapping
    public AdsDto addAd(@ModelAttribute CreateAds ad) {
        return new AdsDto();
    }

    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable Integer id) {
    }
}

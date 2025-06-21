
package ru.skypro.homework.service;

import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;

import java.io.IOException;
import java.util.List;

public interface AdService {
    List<AdsDto> getAllAds();
    AdsDto addAd(CreateAds ad, MultipartFile image);
    void deleteAd(Integer id);
    AdsDto updateAd(Integer id, CreateAds ad);
    ExtendedAd getExtendedAd(Integer id);
    List<AdsDto> getAdsByCurrentUser();
    byte[] updateImage(Integer id, MultipartFile image);
    List<AdsDto> getAdsByEmail(String email);
    AdsDto getAdById(int id);
    byte[] getImage(Integer id);

    void updateAdImage(Integer adId, MultipartFile image) throws IOException;
}


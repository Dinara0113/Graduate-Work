package ru.skypro.homework.mapper;

import org.springframework.stereotype.Component;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;

@Component
public class AdMapperManual {

    public AdsDto toDto(Ad ad) {
        if (ad == null) {
            return null;
        }
        AdsDto dto = new AdsDto();
        dto.setPk(ad.getId());
        dto.setTitle(ad.getTitle());
        dto.setPrice(ad.getPrice());
        // Формируем URL картинки
        dto.setImage("/ads/" + ad.getId() + "/image");
        // Если нужен email автора:
        User author = ad.getAuthor();
        dto.setAuthor(author != null ? author.getEmail() : null);
        return dto;
    }

    public Ad toAd(CreateAds createAds) {
        if (createAds == null) {
            return null;
        }
        Ad ad = new Ad();
        ad.setTitle(createAds.getTitle());
        ad.setDescription(createAds.getDescription());
        ad.setPrice(createAds.getPrice());
        // автор и картинка заполняются отдельно в сервисе!
        return ad;
    }
}

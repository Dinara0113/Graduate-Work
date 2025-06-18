package ru.skypro.homework.security;

import org.springframework.stereotype.Component;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.repository.AdRepository;

@Component("adAccess")
public class AdAccessService {

    private final AdRepository adRepository;

    public AdAccessService(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    public boolean checkAdOwner(String username, Integer adId) {
        Ad ad = adRepository.findById(adId).orElse(null);
        return ad != null && ad.getAuthor().getEmail().equals(username);
    }
}

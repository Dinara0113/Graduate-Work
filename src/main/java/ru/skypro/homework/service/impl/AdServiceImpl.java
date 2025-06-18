package ru.skypro.homework.service.impl;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AdService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdServiceImpl implements AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final AdMapper adMapper;

    public AdServiceImpl(AdRepository adRepository, UserRepository userRepository, AdMapper adMapper) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.adMapper = adMapper;
    }

    @Override
    public List<AdsDto> getAllAds() {
        return adRepository.findAll()
                .stream()
                .map(ad -> {
                    AdsDto dto = adMapper.toDto(ad);
                    dto.setImage("/ads/" + ad.getId() + "/image");
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public AdsDto addAd(CreateAds adDto, MultipartFile image) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        Ad ad = adMapper.toAd(adDto);
        ad.setAuthor(user);

        try {
            ad.setImage(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image", e);
        }

        Ad saved = adRepository.save(ad);
        AdsDto dto = adMapper.toDto(saved);
        dto.setImage("/ads/" + saved.getId() + "/image");
        return dto;
    }

    @Override
    public void deleteAd(Integer id) {
        adRepository.deleteById(id);
    }

    @Override
    public AdsDto updateAd(Integer id, CreateAds adDto) {
        Ad ad = adRepository.findById(id).orElseThrow();
        ad.setTitle(adDto.getTitle());
        ad.setPrice(adDto.getPrice());
        ad.setDescription(adDto.getDescription());
        Ad saved = adRepository.save(ad);
        AdsDto dto = adMapper.toDto(saved);
        dto.setImage("/ads/" + saved.getId() + "/image");
        return dto;
    }

    @Override
    public ExtendedAd getExtendedAd(Integer id) {
        Ad ad = adRepository.findById(id).orElseThrow();
        ExtendedAd extendedAd = new ExtendedAd();
        extendedAd.setPk(ad.getId());
        extendedAd.setTitle(ad.getTitle());
        extendedAd.setDescription(ad.getDescription());
        extendedAd.setPrice(ad.getPrice());
        extendedAd.setImage("/ads/" + ad.getId() + "/image");

        User author = ad.getAuthor();
        extendedAd.setAuthorFirstName(author.getFirstName());
        extendedAd.setAuthorLastName(author.getLastName());
        extendedAd.setEmail(author.getEmail());
        extendedAd.setPhone(author.getPhone());

        return extendedAd;
    }

    @Override
    public List<AdsDto> getAdsByCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return adRepository.findAllByAuthorId(user.getId())
                .stream()
                .map(ad -> {
                    AdsDto dto = adMapper.toDto(ad);
                    dto.setImage("/ads/" + ad.getId() + "/image");
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public byte[] updateImage(Integer id, MultipartFile image) {
        Ad ad = adRepository.findById(id).orElseThrow();
        try {
            ad.setImage(image.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read image", e);
        }
        adRepository.save(ad);
        return ad.getImage();
    }

    @Override
    public List<AdsDto> getAdsByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return adRepository.findAllByAuthorId(user.getId()).stream()
                .map(ad -> {
                    AdsDto dto = adMapper.toDto(ad);
                    dto.setImage("/ads/" + ad.getId() + "/image");
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public AdsDto getAdById(int id) {
        return adRepository.findById(id)
                .map(ad -> {
                    AdsDto dto = adMapper.toDto(ad);
                    dto.setImage("/ads/" + ad.getId() + "/image");
                    return dto;
                })
                .orElseThrow(() -> new RuntimeException("Ad not found"));
    }

    @Override
    public byte[] getImage(Integer id) {
        Ad ad = adRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Объявление не найдено"));
        return ad.getImage();
    }
}

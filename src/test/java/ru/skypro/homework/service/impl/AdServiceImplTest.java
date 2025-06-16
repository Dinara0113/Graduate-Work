package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.homework.dto.*;
import ru.skypro.homework.mapper.AdMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.UserRepository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdServiceImplTest {

    private AdRepository adRepository;
    private UserRepository userRepository;
    private AdMapper adMapper;
    private AdServiceImpl adService;

    @BeforeEach
    void setUp() {
        adRepository = mock(AdRepository.class);
        userRepository = mock(UserRepository.class);
        adMapper = mock(AdMapper.class);
        adService = new AdServiceImpl(adRepository, userRepository, adMapper);
    }

    @Test
    void getAllAds_shouldReturnMappedList() {
        Ad ad = new Ad();
        AdsDto adsDto = new AdsDto();
        when(adRepository.findAll()).thenReturn(List.of(ad));
        when(adMapper.toDto(ad)).thenReturn(adsDto);

        List<AdsDto> result = adService.getAllAds();

        assertEquals(1, result.size());
        assertEquals(adsDto, result.get(0));
    }

    @Test
    void addAd_shouldSaveAdWithImage() throws IOException {
        CreateAds createAds = new CreateAds();
        Ad ad = new Ad();
        MultipartFile file = mock(MultipartFile.class);
        User user = new User();
        AdsDto adsDto = new AdsDto();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(file.getBytes()).thenReturn("image".getBytes());
        when(adMapper.toAd(createAds)).thenReturn(ad);
        when(adRepository.save(any(Ad.class))).thenReturn(ad);
        when(adMapper.toDto(ad)).thenReturn(adsDto);

        AdsDto result = adService.addAd(createAds, file);

        assertEquals(adsDto, result);
        verify(adRepository).save(ad);
        assertEquals(user, ad.getAuthor());
        assertNotNull(ad.getImage());
    }

    @Test
    void deleteAd_shouldCallRepository() {
        adService.deleteAd(1);
        verify(adRepository).deleteById(1);
    }

    @Test
    void updateAd_shouldUpdateAndReturnDto() {
        CreateAds createAds = new CreateAds();
        createAds.setTitle("new");
        createAds.setPrice(123);
        createAds.setDescription("desc");

        Ad ad = new Ad();
        AdsDto dto = new AdsDto();

        when(adRepository.findById(1)).thenReturn(Optional.of(ad));
        when(adRepository.save(ad)).thenReturn(ad);
        when(adMapper.toDto(ad)).thenReturn(dto);

        AdsDto result = adService.updateAd(1, createAds);

        assertEquals(dto, result);
        assertEquals("new", ad.getTitle());
        assertEquals(123, ad.getPrice());
        assertEquals("desc", ad.getDescription());
    }

    @Test
    void getExtendedAd_shouldReturnExtended() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@mail.com");
        user.setPhone("+123");

        Ad ad = new Ad();
        ad.setId(1);
        ad.setTitle("Title");
        ad.setPrice(100);
        ad.setDescription("Desc");
        ad.setAuthor(user);

        when(adRepository.findById(1)).thenReturn(Optional.of(ad));

        ExtendedAd result = adService.getExtendedAd(1);

        assertEquals("Title", result.getTitle());
        assertEquals("John", result.getAuthorFirstName());
        assertEquals("Doe", result.getAuthorLastName());
        assertEquals("john@mail.com", result.getEmail());
        assertEquals("+123", result.getPhone());
    }

    @Test
    void getAdsByCurrentUser_shouldReturnAdsList() {
        User user = new User();
        user.setId(1);
        Ad ad = new Ad();
        AdsDto dto = new AdsDto();

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(adRepository.findAllByAuthorId(1)).thenReturn(List.of(ad));
        when(adMapper.toDto(ad)).thenReturn(dto);

        List<AdsDto> result = adService.getAdsByCurrentUser();

        assertEquals(1, result.size());
        assertEquals(dto, result.get(0));
    }

    @Test
    void updateImage_shouldUpdateAndReturnBytes() throws IOException {
        MultipartFile file = mock(MultipartFile.class);
        Ad ad = new Ad();
        byte[] img = "img".getBytes();

        when(adRepository.findById(1)).thenReturn(Optional.of(ad));
        when(file.getBytes()).thenReturn(img);

        byte[] result = adService.updateImage(1, file);

        assertArrayEquals(img, result);
        verify(adRepository).save(ad);
    }

    @Test
    void getAdsByEmail_shouldReturnUserAds() {
        User user = new User();
        user.setId(1);
        Ad ad = new Ad();
        AdsDto dto = new AdsDto();

        when(userRepository.findByEmail("a@mail.com")).thenReturn(Optional.of(user));
        when(adRepository.findAllByAuthorId(1)).thenReturn(List.of(ad));
        when(adMapper.toDto(ad)).thenReturn(dto);

        List<AdsDto> result = adService.getAdsByEmail("a@mail.com");

        assertEquals(1, result.size());
    }

    @Test
    void getAdById_shouldReturnDto() {
        Ad ad = new Ad();
        AdsDto dto = new AdsDto();

        when(adRepository.findById(5)).thenReturn(Optional.of(ad));
        when(adMapper.toDto(ad)).thenReturn(dto);

        AdsDto result = adService.getAdById(5);

        assertEquals(dto, result);
    }
}

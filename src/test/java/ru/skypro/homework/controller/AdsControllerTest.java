package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.TestSecurityConfig;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.ExtendedAd;
import ru.skypro.homework.dto.ResponseWrapper;
import ru.skypro.homework.service.AdService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user; // ✅ Импорт для авторизации
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdsController.class)
@Import(TestSecurityConfig.class)
public class AdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdService adService;

    private AdsDto testAd;

    private ExtendedAd testExtendedAd;

    @BeforeEach
    void setup() {
        testAd = new AdsDto();
        testAd.setPk(1);
        testAd.setTitle("Test title");
        testAd.setPrice(100);
        testAd.setAuthor("Test author");
        testAd.setImage("image-url");

        testExtendedAd = new ExtendedAd();
        testExtendedAd.setPk(1);
        testExtendedAd.setTitle("Test title");
        testExtendedAd.setPrice(100);
        testExtendedAd.setAuthorFirstName("Test name");
        testExtendedAd.setDescription("Some description");
        testExtendedAd.setEmail("test@gmail.com");
        testExtendedAd.setPhone("+998901234567");
        testExtendedAd.setImage("image-url");
    }

    @Test
    public void testGetAllAds_EmptyList() throws Exception {
        when(adService.getAllAds()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/ads")
                        .with(user("testuser").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    public void testAddAd_Success() throws Exception {
        CreateAds createAds = new CreateAds();
        createAds.setTitle("Test title");
        createAds.setDescription("Description");
        createAds.setPrice(123);

        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", MediaType.IMAGE_JPEG_VALUE, "fake".getBytes());
        MockMultipartFile properties = new MockMultipartFile("properties", "", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createAds));

        when(adService.addAd(any(CreateAds.class), any())).thenReturn(testAd);

        mockMvc.perform(multipart("/ads")
                        .file(image)
                        .file(properties)
                        .with(user("testuser").roles("USER"))
                        .with(req -> { req.setMethod("POST"); return req; }))
                .andExpect(status().isCreated());
    }

    @Test
    public void testDeleteAd_Success() throws Exception {
        doNothing().when(adService).deleteAd(1);

        mockMvc.perform(delete("/ads/{id}", 1)
                        .with(user("testuser").roles("USER")))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetMyAds_ReturnsOk() throws Exception {
        when(adService.getAdsByEmail("user@gmail.com")).thenReturn(List.of(testAd));

        mockMvc.perform(get("/ads/me")
                        .with(user("user@gmail.com").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void testGetAdById_ReturnsOk() throws Exception {
        when(adService.getExtendedAd(1)).thenReturn(testExtendedAd);

        mockMvc.perform(get("/ads/{id}", 1)
                        .with(user("testuser").roles("USER")))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateAdImage_ReturnsOk() throws Exception {
        MockMultipartFile image = new MockMultipartFile("image", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "img".getBytes());
        when(adService.updateImage(eq(1), any())).thenReturn("new-image-url".getBytes());

        mockMvc.perform(multipart("/ads/{id}/image", 1)
                        .file(image)
                        .with(user("testuser").roles("USER"))
                        .with(req -> { req.setMethod("PATCH"); return req; }))
                .andExpect(status().isOk());
    }
}

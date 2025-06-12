package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.skypro.homework.TestSecurityConfig;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.dto.AdsDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdsController.class)
@Import(TestSecurityConfig.class)
public class AdsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllAds_EmptyList() throws Exception {
        mockMvc.perform(get("/ads"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    public void testAddAd_Success() throws Exception {
        mockMvc.perform(multipart("/ads")
                        .param("title", "Test title")
                        .param("description", "Test description")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(request -> {
                            request.setMethod("POST"); // важный хак, т.к. multipart по умолчанию = GET
                            return request;
                        }))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteAd_Success() throws Exception {
        mockMvc.perform(delete("/ads/{id}", 1))
                .andExpect(status().isOk());
    }
}

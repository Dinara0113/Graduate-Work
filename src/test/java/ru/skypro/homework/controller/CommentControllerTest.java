package ru.skypro.homework.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import ru.skypro.homework.TestSecurityConfig;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.dto.ResponseWrapper;
import ru.skypro.homework.service.CommentService;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@Import(TestSecurityConfig.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    private CommentDto testComment;

    @BeforeEach
    void setup() {
        testComment = new CommentDto();
        testComment.setPk(1);
        testComment.setText("Test comment");
        testComment.setAuthor("User");
        testComment.setAuthorImage("img");
    }

    @Test
    void testGetComments_ReturnsOk() throws Exception {
        when(commentService.getComments(1)).thenReturn(List.of(testComment));

        mockMvc.perform(get("/ads/1/comments"))
                .andExpect(status().isOk());
    }

    @Test
    void testAddComment_ReturnsCreated() throws Exception {
        CreateOrUpdateComment request = new CreateOrUpdateComment();
        request.setText("New comment");

        when(commentService.addComment(eq(1), any(CreateOrUpdateComment.class))).thenReturn(testComment);

        mockMvc.perform(post("/ads/1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    void testDeleteComment_ReturnsOk() throws Exception {
        doNothing().when(commentService).deleteComment(1, 10);

        mockMvc.perform(delete("/ads/1/comments/10"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateComment_ReturnsOk() throws Exception {
        CreateOrUpdateComment update = new CreateOrUpdateComment();
        update.setText("Updated comment");

        when(commentService.updateComment(eq(1), eq(10), any(CreateOrUpdateComment.class))).thenReturn(testComment);

        mockMvc.perform(patch("/ads/1/comments/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk());
    }
}

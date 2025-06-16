package ru.skypro.homework.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.Role;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommentServiceImplTest {

    private CommentRepository commentRepository;
    private AdRepository adRepository;
    private UserRepository userRepository;
    private CommentMapper commentMapper;
    private CommentServiceImpl commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        adRepository = mock(AdRepository.class);
        userRepository = mock(UserRepository.class);
        commentMapper = mock(CommentMapper.class);
        commentService = new CommentServiceImpl(commentRepository, adRepository, userRepository, commentMapper);
    }

    @Test
    void getComments_shouldReturnMappedComments() {
        int adId = 1;
        Comment comment = new Comment();
        CommentDto commentDto = new CommentDto();

        when(commentRepository.findAllByAdId(adId)).thenReturn(List.of(comment));
        when(commentMapper.toDto(comment)).thenReturn(commentDto);

        List<CommentDto> result = commentService.getComments(adId);

        assertEquals(1, result.size());
        verify(commentRepository).findAllByAdId(adId);
        verify(commentMapper).toDto(comment);
    }

    @Test
    void addComment_shouldSaveAndReturnDto() {
        int adId = 1;
        CreateOrUpdateComment inputDto = new CreateOrUpdateComment();
        inputDto.setText("Test comment");

        Ad ad = new Ad();
        ad.setId(adId);

        User user = new User(1, "test@mail.com", "pass", "John", "Doe", Role.USER, "123", null);

        Comment comment = new Comment();
        Comment savedComment = new Comment();
        CommentDto resultDto = new CommentDto();

        when(adRepository.findById(adId)).thenReturn(Optional.of(ad));
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(commentMapper.toComment(inputDto)).thenReturn(comment);
        when(commentRepository.save(any(Comment.class))).thenReturn(savedComment);
        when(commentMapper.toDto(savedComment)).thenReturn(resultDto);

        CommentDto result = commentService.addComment(adId, inputDto);

        assertNotNull(result);
        verify(commentRepository).save(any(Comment.class));
        verify(commentMapper).toDto(savedComment);
    }

    @Test
    void deleteComment_shouldCallRepository() {
        int adId = 1;
        int commentId = 100;

        commentService.deleteComment(adId, commentId);

        verify(commentRepository).deleteById(commentId);
    }

    @Test
    void updateComment_shouldUpdateAndReturnDto() {
        int adId = 1;
        int commentId = 10;
        CreateOrUpdateComment update = new CreateOrUpdateComment();
        update.setText("Updated text");

        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setText("Old text");

        Comment updatedComment = new Comment();
        updatedComment.setId(commentId);
        updatedComment.setText("Updated text");

        CommentDto updatedDto = new CommentDto();

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));
        when(commentRepository.save(existingComment)).thenReturn(updatedComment);
        when(commentMapper.toDto(updatedComment)).thenReturn(updatedDto);

        CommentDto result = commentService.updateComment(adId, commentId, update);

        assertNotNull(result);
        assertEquals(updatedDto, result);
        verify(commentRepository).save(existingComment);
    }
}

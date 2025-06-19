package ru.skypro.homework.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.mapper.CommentMapper;
import ru.skypro.homework.model.Ad;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AdRepository;
import ru.skypro.homework.repository.CommentRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.CommentService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для управления комментариями.
 * Предоставляет методы для получения, добавления, удаления и обновления комментариев к объявлениям.
 */
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    public CommentServiceImpl(CommentRepository commentRepository,
                              AdRepository adRepository,
                              UserRepository userRepository,
                              CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.commentMapper = commentMapper;
    }

    /**
     * Возвращает список комментариев к объявлению.
     *
     * @param adId ID объявления
     * @return список {@link CommentDto}
     */
    @Override
    public List<CommentDto> getComments(Integer adId) {
        return commentRepository.findAllByAdId(adId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Добавляет новый комментарий к объявлению.
     *
     * @param adId       ID объявления
     * @param commentDto DTO с текстом комментария
     * @return созданный {@link CommentDto}
     */
    @Override
    public CommentDto addComment(Integer adId, CreateOrUpdateComment commentDto) {
        Ad ad = adRepository.findById(adId).orElseThrow();

        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        Comment comment = commentMapper.toComment(commentDto);
        comment.setAd(ad);
        comment.setAuthor(user);
        comment.setCreatedAt(LocalDateTime.now());

        return commentMapper.toDto(commentRepository.save(comment));
    }

    /**
     * Удаляет комментарий по его ID.
     *
     * @param adId      ID объявления (не используется в логике, добавлен для сигнатуры)
     * @param commentId ID комментария
     */
    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        commentRepository.deleteById(commentId);
    }

    /**
     * Обновляет текст комментария.
     *
     * @param adId       ID объявления (не используется в логике, добавлен для сигнатуры)
     * @param commentId  ID комментария
     * @param commentDto DTO с обновлённым текстом
     * @return обновлённый {@link CommentDto}
     */
    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateComment commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setText(commentDto.getText());
        return commentMapper.toDto(commentRepository.save(comment));
    }
}

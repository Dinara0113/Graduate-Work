package ru.skypro.homework.service.impl;

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

    @Override
    public List<CommentDto> getComments(Integer adId) {
        return commentRepository.findAllByAdId(adId)
                .stream()
                .map(commentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto addComment(Integer adId, CreateOrUpdateComment commentDto) {
        Ad ad = adRepository.findById(adId).orElseThrow();
        User user = userRepository.findAll().stream().findFirst().orElseThrow(); // временно без авторизации

        Comment comment = commentMapper.toComment(commentDto);
        comment.setAd(ad);
        comment.setAuthor(user);
        comment.setCreatedAt(LocalDateTime.now());

        return commentMapper.toDto(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Integer adId, Integer commentId) {
        // можно дополнительно проверить, что комментарий принадлежит этому объявлению
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentDto updateComment(Integer adId, Integer commentId, CreateOrUpdateComment commentDto) {
        Comment comment = commentRepository.findById(commentId).orElseThrow();
        comment.setText(commentDto.getText());
        return commentMapper.toDto(commentRepository.save(comment));
    }
}

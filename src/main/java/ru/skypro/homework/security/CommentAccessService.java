package ru.skypro.homework.security;

import org.springframework.stereotype.Component;
import ru.skypro.homework.model.Comment;
import ru.skypro.homework.repository.CommentRepository;

@Component("commentAccess")
public class CommentAccessService {

    private final CommentRepository commentRepository;

    public CommentAccessService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public boolean checkCommentOwner(String username, Integer commentId) {
        Comment comment = commentRepository.findById(commentId).orElse(null);
        return comment != null && comment.getAuthor().getEmail().equals(username);
    }
}

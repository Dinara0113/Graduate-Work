package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "author", expression = "java(comment.getAuthor() != null ? String.valueOf(comment.getAuthor().getId()) : null)")
    @Mapping(target = "authorImage", expression = "java(comment.getAuthor() != null ? \"/images/user/\" + comment.getAuthor().getId() : null)")
    @Mapping(target = "authorFirstName", expression = "java(comment.getAuthor() != null ? comment.getAuthor().getFirstName() : null)")
    @Mapping(target = "createdAt", expression = "java(comment.getCreatedAt().toInstant(java.time.ZoneOffset.UTC).toEpochMilli())")
    @Mapping(target = "pk", source = "id")
    CommentDto toDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "ad", ignore = true)
    Comment toComment(CreateOrUpdateComment commentDto);
}


package ru.skypro.homework.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CommentsResponse;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import ru.skypro.homework.service.CommentService;

import java.util.List;

/**
 * Контроллер для управления комментариями к объявлениям.
 * Предоставляет эндпоинты для получения, добавления, удаления и обновления комментариев.
 */
@RestController
@RequestMapping("/ads/{adId}/comments")
@CrossOrigin("http://localhost:3000")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * Получение списка комментариев к объявлению.
     *
     * @param adId идентификатор объявления
     * @return список комментариев и их обёртка
     */
    @Operation(summary = "Получение комментариев к объявлению", tags = {"Комментарии"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарии успешно получены")
    })
    @GetMapping
    public ResponseEntity<CommentsResponse> getComments(@PathVariable Integer adId) {
        List<CommentDto> comments = commentService.getComments(adId);
        CommentsResponse response = new CommentsResponse();
        response.setCount(comments.size());
        response.setResults(comments);
        return ResponseEntity.ok(response);
    }

    /**
     * Добавление нового комментария к объявлению.
     *
     * @param adId    идентификатор объявления
     * @param comment текст комментария
     * @return созданный комментарий
     */
    @Operation(summary = "Добавление комментария к объявлению", tags = {"Комментарии"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Комментарий успешно добавлен")
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping
    public ResponseEntity<CommentDto> addComment(@PathVariable Integer adId,
                                                 @RequestBody CreateOrUpdateComment comment) {
        return ResponseEntity.status(201).body(commentService.addComment(adId, comment));
    }

    /**
     * Удаление комментария по его идентификатору.
     *
     * @param adId      идентификатор объявления
     * @param commentId идентификатор комментария
     * @return статус 200 OK при успешном удалении
     */
    @Operation(summary = "Удаление комментария", tags = {"Комментарии"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно удалён")
    })
    @PreAuthorize("hasRole('ADMIN') or @commentAccess.checkCommentOwner(authentication.name, #commentId)")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Integer adId,
                                              @PathVariable Integer commentId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

    /**
     * Обновление текста существующего комментария.
     *
     * @param adId      идентификатор объявления
     * @param commentId идентификатор комментария
     * @param comment   новые данные комментария
     * @return обновлённый комментарий
     */
    @Operation(summary = "Обновление комментария", tags = {"Комментарии"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Комментарий успешно обновлён")
    })
    @PreAuthorize("hasRole('ADMIN') or @commentAccess.checkCommentOwner(authentication.name, #commentId)")
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Integer adId,
                                                    @PathVariable Integer commentId,
                                                    @RequestBody CreateOrUpdateComment comment) {
        return ResponseEntity.ok(commentService.updateComment(adId, commentId, comment));
    }
}

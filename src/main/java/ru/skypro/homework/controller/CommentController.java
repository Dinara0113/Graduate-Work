package ru.skypro.homework.controller;

import org.springframework.web.bind.annotation.*;
import ru.skypro.homework.dto.CommentDto;
import ru.skypro.homework.dto.CreateOrUpdateComment;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/ads/{adId}/comments")
@CrossOrigin("http://localhost:3000")
public class CommentController {

    @GetMapping
    public List<CommentDto> getComments(@PathVariable Integer adId) {
        return Collections.emptyList();
    }

    @PostMapping
    public CommentDto addComment(@PathVariable Integer adId, @RequestBody CreateOrUpdateComment comment) {
        return new CommentDto();
    }
}

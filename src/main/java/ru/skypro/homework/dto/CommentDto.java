package ru.skypro.homework.dto;

import java.time.LocalDateTime;

public class CommentDto {
    private Integer pk;
    private String author;
    private String authorImage;
    private LocalDateTime createdAt;
    private String text;

    public CommentDto() {
    }

    public Integer getPk() {
        return pk;
    }

    public void setPk(Integer pk) {
        this.pk = pk;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorImage() {
        return authorImage;
    }

    public void setAuthorImage(String authorImage) {
        this.authorImage = authorImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

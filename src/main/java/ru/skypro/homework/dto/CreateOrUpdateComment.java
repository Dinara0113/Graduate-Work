package ru.skypro.homework.dto;

public class CreateOrUpdateComment {
    private String text;

    public CreateOrUpdateComment() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

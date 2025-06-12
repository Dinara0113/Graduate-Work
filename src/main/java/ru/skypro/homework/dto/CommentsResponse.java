package ru.skypro.homework.dto;

import java.util.List;

public class CommentsResponse {
    private int count;
    private List<CommentDto> results;

    public CommentsResponse() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<CommentDto> getResults() {
        return results;
    }

    public void setResults(List<CommentDto> results) {
        this.results = results;
    }
}

package ru.skypro.homework.dto;

import java.util.List;

public class ResponseWrapper<T> {
    private int count;
    private List<T> results;

    public ResponseWrapper() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }
}

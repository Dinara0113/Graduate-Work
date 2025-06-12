package ru.skypro.homework.dto;

import java.util.List;

public class AdsResponse {
    private int count;
    private List<AdsDto> results;

    public AdsResponse() {
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<AdsDto> getResults() {
        return results;
    }

    public void setResults(List<AdsDto> results) {
        this.results = results;
    }
}

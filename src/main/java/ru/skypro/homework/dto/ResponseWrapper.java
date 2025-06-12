package ru.skypro.homework.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResponseWrapper<T> {
    private int count;
    private List<T> results;
}

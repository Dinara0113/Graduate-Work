package ru.skypro.homework.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO для создания или обновления объявления")
public class CreateAds {
    @Schema(description = "Заголовок", example = "Продам велосипед", minLength = 4, maxLength = 32)
    private String title;

    @Schema(description = "Цена", example = "15000")
    private Integer price;

    @Schema(description = "Описание", example = "Велосипед в хорошем состоянии", minLength = 8, maxLength = 64)
    private String description;
    public CreateAds() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

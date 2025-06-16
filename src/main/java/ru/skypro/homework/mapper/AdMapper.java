package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.model.Ad;

@Mapper(componentModel = "spring")
public interface AdMapper {

    @Mapping(target = "pk", source = "id")
    @Mapping(target = "author", expression = "java(ad.getAuthor().getEmail())")
    @Mapping(target = "image", expression = "java(new String(ad.getImage()))")
    AdsDto toDto(Ad ad);

    Ad toAd(CreateAds createAds);

    CreateAds toCreateAds(Ad ad);
}

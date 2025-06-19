package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.skypro.homework.dto.AdsDto;
import ru.skypro.homework.dto.CreateAds;
import ru.skypro.homework.model.Ad;

@Mapper(componentModel = "spring")
public interface AdMapper {

    @Mappings({
            @Mapping(target = "pk", source = "id"),
            @Mapping(target = "author", expression = "java(ad.getAuthor().getEmail())"),
            @Mapping(target = "image", expression = "java(\"/images/ad/\" + ad.getId())")
    })
    AdsDto toDto(Ad ad);

    Ad toAd(CreateAds createAds);

    CreateAds toCreateAds(Ad ad);
}

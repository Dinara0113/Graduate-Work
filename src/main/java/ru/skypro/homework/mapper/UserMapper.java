package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.skypro.homework.dto.CreateUser;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.User;

import java.util.Base64;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "image", source = "image", qualifiedByName = "byteArrayToBase64")
    UserDto toDto(User user);

    @Mapping(target = "image", source = "image", qualifiedByName = "base64ToByteArray")
    User toEntity(UserDto dto);

    User toEntity(CreateUser dto);

    User toEntity(UpdateUser dto);

    @Named("byteArrayToBase64")
    default String byteArrayToBase64(byte[] image) {
        return image != null ? Base64.getEncoder().encodeToString(image) : null;
    }

    @Named("base64ToByteArray")
    default byte[] base64ToByteArray(String image) {
        return image != null ? Base64.getDecoder().decode(image) : null;
    }
}

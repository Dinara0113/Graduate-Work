package ru.skypro.homework.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skypro.homework.dto.CreateUser;
import ru.skypro.homework.dto.UpdateUser;
import ru.skypro.homework.dto.UserDto;
import ru.skypro.homework.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "image", expression = "java(\"/images/user/\" + user.getId())")
    UserDto toDto(User user);

    @Mapping(target = "image", ignore = true)
    User toEntity(UserDto dto);

    User toEntity(CreateUser dto);

    User toEntity(UpdateUser dto);
}

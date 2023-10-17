package com.example.hospital.mapper;

import com.example.hospital.dto.UserChangeDto;
import com.example.hospital.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "id",source = "id")
    @Mapping(target = "image",ignore = true)
    UserChangeDto userToUserChangeDto(User user);


    @Mapping(target = "id",source = "id")
    @Mapping(target = "image",ignore = true)
    User userChangeDtoToUser(UserChangeDto userChangeDto);
}

package com.appswave.newsmanagement.config.mapper;

import com.appswave.newsmanagement.dto.UserDto;
import com.appswave.newsmanagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {

    UserDto userToUserDto(User user);

    User userDtoToUser(UserDto userDto);

    //UserResponseDto userToUserResponseDto(User user);
}


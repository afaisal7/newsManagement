package com.appswave.newsmanagement.mapper;

import com.appswave.newsmanagement.dto.UserDto;
import com.appswave.newsmanagement.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    @Mapping(source = "role", target = "role")
    UserDto toDto(User user);
}


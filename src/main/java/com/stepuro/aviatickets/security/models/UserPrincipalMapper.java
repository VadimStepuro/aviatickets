package com.stepuro.aviatickets.security.models;

import com.stepuro.aviatickets.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserPrincipalMapper {
    UserPrincipalMapper INSTANCE = Mappers.getMapper(UserPrincipalMapper.class);

    UserPrincipal userToUserPrincipal(User user);

    User userPrincipalToUser(UserPrincipal user);
}

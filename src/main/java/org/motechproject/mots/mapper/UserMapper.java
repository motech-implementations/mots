package org.motechproject.mots.mapper;

import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.dto.UserDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  UserDto toDto(User user);

  @Mapping(target = "id", ignore = true)
  User fromDto(UserDto userDto);

  List<UserDto> toDtos(Iterable<User> users);

  @Mapping(target = "id", ignore = true)
  void updateFromDto(UserDto userDto, @MappingTarget User user);
}
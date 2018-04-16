package org.motechproject.mots.mapper;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.motechproject.mots.domain.security.User;
import org.motechproject.mots.dto.UserDto;
import org.motechproject.mots.dto.UserProfileDto;

@Mapper(uses = { UuidMapper.class },
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

  String PASSWORD = "password";

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  @Mappings({
      @Mapping(target = PASSWORD, ignore = true),
      @Mapping(target = "passwordConfirm", ignore = true)
  })
  UserDto toDto(User user);

  @Mappings({
      @Mapping(target = PASSWORD, ignore = true),
  })
  UserProfileDto toUserProfileDto(User user);

  @Mapping(target = "id", ignore = true)
  User fromDto(UserDto userDto);

  List<UserDto> toDtos(Iterable<User> users);

  /**
   * Updates user with userDto. Sets password if it's present in userDto.
   * @param userDto data transfer object serialized from frontend.
   * @param user user to be updated.
   */
  default void passwordNullSafeUpdateFromDto(UserDto userDto, @MappingTarget User user) {
    updateFromDto(userDto, user);
    if (StringUtils.isNotBlank(userDto.getPassword())) {
      user.setPassword(userDto.getPassword());
    }
  }

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = PASSWORD, ignore = true),
  })
  void updateFromDto(UserDto userDto, @MappingTarget User user);

  @Mappings({
      @Mapping(target = "id", ignore = true),
      @Mapping(target = PASSWORD, ignore = true),
      @Mapping(target = "username", ignore = true),
  })
  void updateFromUserProfileDto(UserProfileDto userProfileDto, @MappingTarget User user);

}

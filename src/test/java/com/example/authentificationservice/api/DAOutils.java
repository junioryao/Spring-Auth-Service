package com.example.authentificationservice.api;

import com.example.authentificationservice.dto.RoleDto;
import com.example.authentificationservice.dto.UserDto;
import com.example.authentificationservice.model.UserRole;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.List;

@RequiredArgsConstructor
public class DAOutils {

  public static UserDto getUserDTO() {

    var userRole = new ModelMapper().map(RoleDto.builder().name("ADMIN").build(), UserRole.class);
    return UserDto.builder()
        .userLastName("yao")
        .userName("junior")
        .email("junioryao.jy@gmail.com")
        .Roles(List.of(userRole))
        .build();
  }
}

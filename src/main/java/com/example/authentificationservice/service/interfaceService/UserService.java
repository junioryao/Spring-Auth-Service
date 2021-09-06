package com.example.authentificationservice.service.interfaceService;

import com.example.authentificationservice.ExceptionHandler.UserNotFound;
import com.example.authentificationservice.dto.RoleDto;
import com.example.authentificationservice.dto.UserDto;

import java.util.List;

public interface UserService {

  UserDto saveUser(UserDto userDto);

  RoleDto saveUserRole(RoleDto roleDta);

  UserDto getUser(String name);

  List<UserDto> getUsers();

  void addUserRole(String userId, String roleName) throws UserNotFound;
}

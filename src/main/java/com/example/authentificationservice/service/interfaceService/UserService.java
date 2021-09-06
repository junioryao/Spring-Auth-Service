package com.example.authentificationservice.service.interfaceService;

import com.example.authentificationservice.ExceptionHandler.UserNotFound;
import com.example.authentificationservice.dto.RoleDto;
import com.example.authentificationservice.dto.UserDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface UserService {

  UserDto saveUser(UserDto userDto);

  RoleDto saveUserRole(RoleDto roleDta);

  UserDto getUser(String name);

  List<UserDto> getUsers();

  void refreshUserToken(HttpServletRequest request, HttpServletResponse response) throws Exception;

  void addUserRole(String userId, String roleName) throws UserNotFound;
}

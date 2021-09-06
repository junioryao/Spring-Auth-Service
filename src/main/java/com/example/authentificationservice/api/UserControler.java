package com.example.authentificationservice.api;

import com.example.authentificationservice.ExceptionHandler.UserNotFound;
import com.example.authentificationservice.dto.RoleDto;
import com.example.authentificationservice.dto.UserDto;
import com.example.authentificationservice.service.UserServiceImplementation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/v2/user")
@RequiredArgsConstructor
@Slf4j
public class UserControler {

  private final UserServiceImplementation userServiceImplementation;

  @GetMapping()
  public ResponseEntity<UserDto> getAUserDetails(@RequestParam String name) {
    return ResponseEntity.ok().body(userServiceImplementation.getUser(name));
  }

  @GetMapping("/all")
  public ResponseEntity<List<UserDto>> getAllUserDetails() {
    return ResponseEntity.ok().body(userServiceImplementation.getUsers());
  }

  @PutMapping()
  public ResponseEntity.BodyBuilder putRoleToUser(
      @RequestParam String name, @RequestParam String role) throws UserNotFound {
    userServiceImplementation.addUserRole(name, role);
    return ResponseEntity.ok();
  }

  @PutMapping("/role/{rolename}")
  public ResponseEntity.BodyBuilder insertANewRole(@RequestBody RoleDto roleDta) {

    userServiceImplementation.saveUserRole(roleDta);
    return ResponseEntity.ok();
  }

  @PostMapping("register")
  public ResponseEntity<UserDto> SaveAnewUserToBDD(@RequestBody UserDto userDto) {
    log.info("about to register the user ");

    log.info(userDto.toString());
    return ResponseEntity.ok(userServiceImplementation.saveUser(userDto));
  }

  @GetMapping("/refreshToken")
  public void refreshUserToken(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    userServiceImplementation.refreshUserToken(request, response);
  }
}

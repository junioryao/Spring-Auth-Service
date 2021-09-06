package com.example.authentificationservice.service;

import com.example.authentificationservice.ExceptionHandler.UserNotFound;
import com.example.authentificationservice.dto.RoleDto;
import com.example.authentificationservice.dto.UserDto;
import com.example.authentificationservice.model.User;
import com.example.authentificationservice.model.UserRole;
import com.example.authentificationservice.repository.RoleRepository;
import com.example.authentificationservice.repository.UserRepository;
import com.example.authentificationservice.security.JWT.JWTProcess;
import com.example.authentificationservice.service.interfaceService.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImplementation implements UserService, UserDetailsService {
  private final JWTProcess jwtProcess;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public UserDto saveUser(UserDto userDto) {
    // encrypt the user password
    userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
    // RoleDto roleDto =  new RoleDto(userDto.getRoles())
    var user = userRepository.save(modelMapper.map(userDto, User.class));

    log.info("user saved");
    return modelMapper.map(user, UserDto.class);
  }

  @Override
  public RoleDto saveUserRole(RoleDto roleDta) {
    log.info("role saved");
    var roleSaved = roleRepository.save(new ModelMapper().map(roleDta, UserRole.class));
    return modelMapper.map(roleSaved, RoleDto.class);
  }

  @Override
  public UserDto getUser(String name) {
    log.info("get user info");
    return modelMapper.map(userRepository.findByUserName(name), UserDto.class);
  }

  @Override
  public List<UserDto> getUsers() {
    log.info("get all users");
    return modelMapper.map(userRepository.findAll(), (Type) UserDto.class);
  }

  @Override
  public void refreshUserToken(HttpServletRequest request, HttpServletResponse response)
      throws Exception {
    String authorizationHeader = request.getHeader(AUTHORIZATION);
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
      try {
        String TOKEN = authorizationHeader.split(" ")[1];
        String newAccessToken = jwtProcess.refreshJWTToken(TOKEN, this);
        response.setHeader("accessToken", newAccessToken);
      } catch (Exception e) {
        throw new Exception(e.getMessage());
      }
    }
  }

  @Override
  @Transactional
  public void addUserRole(String userId, String roleName) throws UserNotFound {
    log.info("add role to user ");
    var userDetail =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new UserNotFound("this user Id is not found"));
    var rolesdetails = roleRepository.findByName(roleName);
    userDetail.getRoles().add(rolesdetails);
  }

  @Override
  public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
    log.info("in user details service " + userName);
    // define how spring should look up for the user
    var authUser =
        userRepository
            .findByUserName(userName)
            .orElseThrow(() -> new UsernameNotFoundException("Can not identify the user"));
    // else create an object of user detail service
    // pass an object of granted authority
    Collection<SimpleGrantedAuthority> authorityCollection = new ArrayList<>();
    authUser
        .getRoles()
        .forEach(
            userRole -> {
              authorityCollection.add(new SimpleGrantedAuthority(userRole.getName()));
            });

    // create the userdetails service
    UserDetails user =
        new org.springframework.security.core.userdetails.User(
            authUser.getUserName(), authUser.getPassword(), authorityCollection);
    return user;
  }
}

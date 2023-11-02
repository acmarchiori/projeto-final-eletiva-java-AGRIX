package com.betrybe.agrix.controller;

import com.betrybe.agrix.controller.dto.AuthenticationDto;
import com.betrybe.agrix.models.entities.Person;
import com.betrybe.agrix.service.PersonService;
import com.betrybe.agrix.service.TokenService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Authentication controller class.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final PersonService personService;
  private final TokenService tokenService;

  /**
   * Constructor.
   */
  @Autowired
  public AuthenticationController(
      AuthenticationManager authenticationManager,
      PersonService personService,
      TokenService tokenService) {
    this.authenticationManager = authenticationManager;
    this.personService = personService;
    this.tokenService = tokenService;
  }

  /**
   * Register a person.
   */
  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(
      @RequestBody AuthenticationDto authenticationDto) {
    UsernamePasswordAuthenticationToken usernamePassword =
        new UsernamePasswordAuthenticationToken(
            authenticationDto.username(),
            authenticationDto.password());
    Authentication auth = authenticationManager.authenticate(usernamePassword);
    Person person = (Person) auth.getPrincipal();

    String token = tokenService.generateToken(person);
    Map<String, String> response = new HashMap<>();
    response.put("token", token);

    return ResponseEntity.ok(response);
  }
}

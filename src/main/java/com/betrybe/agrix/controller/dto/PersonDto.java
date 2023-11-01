package com.betrybe.agrix.controller.dto;

import com.betrybe.agrix.models.entities.Person;
import com.betrybe.agrix.security.Role;

/**
 * Person DTO.
 */
public record PersonDto(Long id, String username, Role role) {
  public Person toPerson() {
    return new Person(username, null, role);
  }
}

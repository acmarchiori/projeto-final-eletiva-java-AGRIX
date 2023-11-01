package com.betrybe.agrix.controller;

import com.betrybe.agrix.controller.dto.PersonDto;
import com.betrybe.agrix.models.entities.Person;
import com.betrybe.agrix.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Person controller.
 */
@RestController
@RequestMapping("/persons")
public class PersonController {

  private final PersonService personService;

  @Autowired
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  /**
   * Create a person.
   */
  @PostMapping
  public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto personDto) {
    Person person = personDto.toPerson();
    Person createdPerson = personService.create(person);

    PersonDto createdPersonDto = new PersonDto(
        createdPerson.getId(),
        createdPerson.getUsername(),
        createdPerson.getRole()
    );

    return ResponseEntity.status(HttpStatus.CREATED).body(createdPersonDto);
  }
}

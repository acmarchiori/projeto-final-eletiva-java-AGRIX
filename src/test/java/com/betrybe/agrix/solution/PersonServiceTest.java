package com.betrybe.agrix.solution;


import com.betrybe.agrix.exception.PersonNotFoundException;
import com.betrybe.agrix.models.entities.Person;
import com.betrybe.agrix.models.repositories.PersonRepository;
import com.betrybe.agrix.service.PersonService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PersonServiceTest {

  @Autowired
  PersonService personService;

  @MockBean
  private PersonRepository personRepository;

  @Test
  public void testGetPersonByIdExistingPerson() {
    // Arrange
    Long id = 1L;
    Person person = new Person();
    person.setId(id);

    when(personRepository.findById(id)).thenReturn(Optional.of(person));

    // Act
    Person result = personService.getPersonById(id);

    // Assert
    assertNotNull(result);
    assertEquals(id, result.getId());
  }

  @Test
  public void testGetPersonByIdNonExistingPerson() {
    // Arrange
    Long id = 1L;
    when(personRepository.findById(id)).thenReturn(Optional.empty());

    // Act and Assert
    assertThrows(PersonNotFoundException.class, () -> personService.getPersonById(id));
  }

  @Test
  public void testGetPersonByUsernameExistingPerson() {
    // Arrange
    String username = "testUser";
    Person person = new Person();
    person.setUsername(username);

    when(personRepository.findByUsername(username)).thenReturn(Optional.of(person));

    // Act
    Person result = personService.getPersonByUsername(username);

    // Assert
    assertNotNull(result);
    assertEquals(username, result.getUsername());
  }

  @Test
  public void testGetPersonByUsernameNonExistingPerson() {
    // Arrange
    String username = "testUser";
    when(personRepository.findByUsername(username)).thenReturn(Optional.empty());

    // Act and Assert
    assertThrows(PersonNotFoundException.class, () -> personService.getPersonByUsername(username));
  }

  @Test
  public void testCreatePerson() {
    // Arrange
    Person personToCreate = new Person();
    when(personRepository.save(any(Person.class))).thenReturn(personToCreate);

    // Act
    Person result = personService.create(personToCreate);

    // Assert
    assertNotNull(result);
  }
}

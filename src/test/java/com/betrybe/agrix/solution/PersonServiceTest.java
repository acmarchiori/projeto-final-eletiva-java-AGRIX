package com.betrybe.agrix.solution;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.betrybe.agrix.exception.PersonNotFoundException;
import com.betrybe.agrix.models.entities.Person;
import com.betrybe.agrix.models.repositories.PersonRepository;
import com.betrybe.agrix.service.PersonService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
  public void testLoadUserByUsernameExistingPerson() {
    // Arrange
    String username = "testUser";
    Person person = new Person();
    person.setUsername(username);

    when(personRepository.findByUsername(username)).thenReturn(person);

    // Act
    UserDetails result = personService.loadUserByUsername(username);

    // Assert
    assertNotNull(result);
    assertEquals(username, result.getUsername());
  }
}

package ht.ueh.first.spring.restatm.services;

import ht.ueh.first.spring.restatm.models.people.Person;
import ht.ueh.first.spring.restatm.repositories.PeopleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeopleService {
    private final PeopleRepository peopleRepository;

    /**
     * Constructor for PeopleService.
     *
     * @param peopleRepository the repository used to manage people data
     */
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    /**
     * Retrieves all people from the repository.
     *
     * @return a list of Person objects
     */
    public List<Person> getAllPeople() {
        return peopleRepository.getPeople();
    }

    public void addPerson(Person person) {
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        if (person.getFirstName() != null) {
            throw new IllegalArgumentException("Person first name cannot be null");
        }
        if (person.getLastName() != null) {
            throw new IllegalArgumentException("Person last name cannot be null");
        }
        if (peopleRepository.getPerson(person.getFirstName()).isPresent()) {
            throw new IllegalArgumentException("Person already exists");
        }
        peopleRepository.addPerson(person);
    }
}
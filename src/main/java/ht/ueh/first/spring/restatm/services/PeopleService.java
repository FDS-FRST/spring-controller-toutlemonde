package ht.ueh.first.spring.restatm.services;

import ht.ueh.first.spring.restatm.models.people.Person;
import ht.ueh.first.spring.restatm.repositories.PeopleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PeopleService {
    private final PeopleRepository peopleRepository;

    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

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
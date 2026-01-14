package ht.ueh.first.spring.restatm.repositories;

import ht.ueh.first.spring.restatm.models.people.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Repository
public class PeopleRepository {
    private final List<Person> people;

    public PeopleRepository() {
        people = List.of(
                new Person("John", "Doe"),
                new Person("Jane", "Doe")
        );
    }

    /**
     * Retrieves the list of people managed by this repository.
     *
     * @return a list of Person objects representing the people in the repository
     */
    public List<Person> getPeople() {
        return people;
    }

    /**
     * Adds a new person to the list of people managed by this repository.
     * @param person the person to add
     */
    public void addPerson(Person person) {
        people.add(person);
    }

    public Optional<Person> getPerson(String firstName) {
        return people.stream().filter(new Predicate<Person>() {
            @Override
            public boolean test(Person person) {
                return person.getFirstName().equals(firstName);
            }
        }).findFirst();
    }
}
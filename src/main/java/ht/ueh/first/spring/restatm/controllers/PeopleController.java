package ht.ueh.first.spring.restatm.controllers;

import ht.ueh.first.spring.restatm.models.persons.Person;
import ht.ueh.first.spring.restatm.services.PeopleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/people")
public class PeopleController {
    private final PeopleService peopleService;

    public PeopleController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping
    public ResponseEntity<List<Person>> getAllPeople() {
        List<Person> people = peopleService.getAllPeople();
        return ResponseEntity.ok(people);
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        peopleService.addPerson(person);
        return ResponseEntity.ok(person);
    }
}

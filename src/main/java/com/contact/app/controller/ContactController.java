package com.contact.app.controller;

import com.contact.app.model.Person;
import com.contact.app.repository.ContactRepository;
import com.contact.app.service.ContactService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contact")
public class ContactController {

    ContactService contactService;
    ContactRepository contactRepository;

    @PostMapping("/save")
    public ResponseEntity<Person> save(Person person){
        Person saved = contactRepository.save(person);
        return new ResponseEntity(saved, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/retrieve/{id}")
    public ResponseEntity<Person> retrieve(Integer id){
        Person person = contactService.retrieve(id);
        return new ResponseEntity(person, HttpStatus.OK);
    }
}

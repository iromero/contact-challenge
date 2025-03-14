package com.contact.app.service;

import com.contact.app.model.Person;
import com.contact.app.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    @Autowired
    ContactRepository contactRepository;

    public Person retrieve(Integer id){
        return contactRepository.findById(id).get();
    }
}
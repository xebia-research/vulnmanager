package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.models.company.Person;
import com.xebia.vulnmanager.repositories.PersonRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class PersonController {

    private PersonRepository personRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public PersonController(PersonRepository personRepository,
                            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.personRepository = personRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping("/sign-up")
    public void signUp(@RequestBody Person user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        personRepository.save(user);
    }
}

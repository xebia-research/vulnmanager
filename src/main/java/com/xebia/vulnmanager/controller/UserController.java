package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.models.company.Person;
import com.xebia.vulnmanager.models.company.PersonLogin;
import com.xebia.vulnmanager.repositories.PersonRepository;
import com.xebia.vulnmanager.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/users")
public class UserController {
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private CompanyService companyService;

    @Autowired
    public UserController(final PersonRepository personRepository,
                          final BCryptPasswordEncoder bCryptPasswordEncoder,
                          final CompanyService companyService) {
        this.personRepository = personRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.companyService = companyService;
    }

    @RequestMapping(value = "sign-up", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Person> signUp(@RequestBody PersonLogin user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Person person = new Person(user.getUsername(),
                                   user.getPassword(),
                                   companyService.getCompanyByName(user.getCompanyName()));
        person = personRepository.save(person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }
}

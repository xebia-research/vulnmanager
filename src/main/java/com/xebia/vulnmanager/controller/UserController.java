package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.models.company.Person;
import com.xebia.vulnmanager.models.company.PersonLogin;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.repositories.PersonRepository;
import com.xebia.vulnmanager.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @RequestMapping(value = "whoami", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> whoami() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Person p = personRepository.findByUsername(currentPrincipalName);
        if (p == null) {
            return new ResponseEntity<>(new ErrorMsg("User not found!?"), HttpStatus.OK);
        } else {
            p.setPassword("");
            return new ResponseEntity<>(p, HttpStatus.OK);
        }
    }


    @RequestMapping(value = "sign-up", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> signUp(@RequestBody PersonLogin user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        Person person = new Person(user.getUsername(),
                                   user.getPassword(),
                                   companyService.getCompanyByName(user.getCompanyName()));

        if (personRepository.findByUsername(person.getUsername()) != null) {
            return new ResponseEntity<>(new ErrorMsg("Username already in use"), HttpStatus.BAD_REQUEST);
        }
        person = personRepository.save(person);
        return new ResponseEntity<>(person, HttpStatus.OK);
    }
}

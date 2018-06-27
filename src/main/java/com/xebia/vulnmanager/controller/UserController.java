package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.security.RandomAESKeyGen;
import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Person;
import com.xebia.vulnmanager.models.company.PersonLogin;
import com.xebia.vulnmanager.models.company.Team;
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

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping(value = "/users")
public class UserController {
    private static final int KEY_LENGTH = 256;
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
            return new ResponseEntity<>(p.getDetailedPerson(), HttpStatus.OK);
        }
    }

    private Person findPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Person p = personRepository.findByUsername(currentPrincipalName);
        if (p != null) {
            p.setPassword("");
        }
        return p;
    }

    @RequestMapping(value = "whomycompany", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> whoMyCompany() {
        Person p = findPerson();
        if (p == null) {
            return new ResponseEntity<>(new ErrorMsg("User not found!?"), HttpStatus.OK);
        } else {
            List<Company> companyList = companyService.getCompanyRepository().findAll();

            if (companyList.size() == 0) {
                return new ResponseEntity<>(new ErrorMsg("No companies"), HttpStatus.OK);
            }

            for (Company company : companyList) {
                List<Person> employees = company.getEmployees();

                for (Person empl : employees) {
                    if (empl.getId().equals(p.getId())) {
                        return new ResponseEntity<>(company, HttpStatus.OK);
                    }
                }

            }

            return new ResponseEntity<>(new ErrorMsg("No companies"), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "whomyteams", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<?> whoMyTeams() {
        Person p = findPerson();
        if (p == null) {
            return new ResponseEntity<>(new ErrorMsg("User not found!?"), HttpStatus.OK);
        } else {
            List<Company> companyList = companyService.getCompanyRepository().findAll();

            if (companyList.size() == 0) {
                return new ResponseEntity<>(new ErrorMsg("No companies"), HttpStatus.OK);
            }

            List<Team> returnTeams = new ArrayList<>();

            for (Company company : companyList) {
                List<Team> teams = company.getTeams();

                for (Team team : teams) {
                    List<Person> members = team.getTeamMembers();
                    for (Person member : members) {
                        if (member.getId().equals(p.getId())) {
                            returnTeams.add(team);
                            break;
                        }
                    }
                }

            }

            return new ResponseEntity<>(returnTeams, HttpStatus.OK);
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

        try {
            person.setApiKey(RandomAESKeyGen.generate(KEY_LENGTH));
        } catch (NoSuchAlgorithmException e) {
            return new ResponseEntity<>(new ErrorMsg("Couldn\'t generate token for new user. user not created"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        person = personRepository.save(person);
        return new ResponseEntity<>(person.getDetailedPerson(), HttpStatus.OK);
    }
}

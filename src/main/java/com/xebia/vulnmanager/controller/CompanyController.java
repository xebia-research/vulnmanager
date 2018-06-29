package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Person;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.repositories.PersonRepository;
import com.xebia.vulnmanager.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping(value = "/{company}")
public class CompanyController {
    private static final String IS_AUTHENTICATED_STRING = "isAuthenticated";
    private static final String AUTH_NOT_CORRECT_STRING = "Auth not correct!";
    private static final String COMPANY = "company";

    private CompanyService companyService;

    private PersonRepository userService;

    @Autowired
    public CompanyController(final CompanyService companyService, final PersonRepository userService) {
        this.companyService = companyService;
        this.userService = userService;
    }

    /**
     * Get the teams of a specific company
     * @param companyName The name of the company
     * @return A list of teams within the team
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getCompany(@PathVariable(COMPANY) String companyName) {
        // find company by name
        Company foundCompany = companyService.getCompanyByName(companyName);

        if (foundCompany == null) {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Company not found"), HttpStatus.NOT_FOUND);
        }
            return new ResponseEntity<Company>(foundCompany, HttpStatus.OK);
    }

    /**
     * Get the teams of a specific company
     * @param companyName The name of the company
     * @return A list of teams within the team
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> createCompany(@PathVariable(COMPANY) String companyName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Person creator = userService.findByUsername(currentPrincipalName);

        if (creator == null) {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("User does not exist"), HttpStatus.OK);
        }

        // find company by name
        Company foundCompany = companyService.getCompanyByName(companyName);

        if (foundCompany == null) {
            Company company = new Company(companyName);
            company.addEmployee(creator);
            creator.setCompany(company);
            return new ResponseEntity<>(companyService.getCompanyRepository().save(company), HttpStatus.OK);
        } else {
            foundCompany.addEmployee(creator);
            creator.setCompany(foundCompany);
            foundCompany = companyService.getCompanyRepository().save(foundCompany);
            return new ResponseEntity<>(foundCompany, HttpStatus.OK);

        }
    }

    /**
     * Get the teams of a specific company
     * @param companyName The name of the company
     * @return A list of teams within the team
     */
    @RequestMapping(value = "/{team}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getCompanyTeamMembers(@PathVariable(COMPANY) String companyName, @PathVariable("team") String teamName) {
        // find company by name
        Company foundCompany = companyService.getCompanyByName(companyName);

        if (foundCompany == null) {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Company not found"), HttpStatus.NOT_FOUND);
        }

        Team team = companyService.getTeamOfCompany(companyName, teamName);
        if (team != null) {
            return new ResponseEntity<Team>(team, HttpStatus.OK);
        } else {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Team not found"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get the teams of a specific company
     * @param companyName The name of the company
     * @return A list of teams within the team
     */
    @RequestMapping(value = "/{team}", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> createTeam(@PathVariable(COMPANY) String companyName, @PathVariable("team") String teamName) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();

        Person creator = userService.findByUsername(currentPrincipalName);

        if (creator == null) {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("User does not exist"), HttpStatus.OK);
        }


        // find company by name
        Company foundCompany = companyService.getCompanyByName(companyName);

        if (foundCompany == null) {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Company not found"), HttpStatus.NOT_FOUND);
        }

        Team team = companyService.getTeamOfCompany(companyName, teamName);
        if (team != null) {
            if (team.addTeamMember(creator)) {
                foundCompany.addTeam(team);
                return new ResponseEntity<Company>(companyService.getCompanyRepository().save(foundCompany), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ErrorMsg("Already in team"), HttpStatus.OK);

            }
        } else {
            Team newTeam = new Team(teamName);
            newTeam.addTeamMember(creator);
            newTeam.setCompany(foundCompany);
            foundCompany.addTeam(newTeam);
            return new ResponseEntity<Company>(companyService.getCompanyRepository().save(foundCompany), HttpStatus.OK);
        }
    }
}

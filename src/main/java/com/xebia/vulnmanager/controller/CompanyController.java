package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/{company}")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    /**
     * Get the teams of a specific company
     * @param authKey The auth key of the company used to auth the request
     * @param companyName The name of the company
     * @return A list of teams within the team
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getCompanyTeams(@RequestHeader(value = "auth", defaultValue = "nope") String authKey, @PathVariable("company") String companyName) {
        // find company by name
        Company foundCompany = findCompanyByName(companyName);

        if (foundCompany == null) {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Company not found"), HttpStatus.NOT_FOUND);
        }

        // check auth
        if (authKey.equals(foundCompany.getAuthKey())) {
            return new ResponseEntity<Company>(foundCompany, HttpStatus.OK);
        } else {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Wrong auth key"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get the teams of a specific company
     * @param authKey The auth key of the company used to auth the request
     * @param companyName The name of the company
     * @return A list of teams within the team
     */
    @RequestMapping(value = "/{team}", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getCompanyTeamMembers(@RequestHeader(value = "auth", defaultValue = "nope") String authKey, @PathVariable("company") String companyName, @PathVariable("team") String teamName) {
        // find company by name
        Company foundCompany = findCompanyByName(companyName);

        if (foundCompany == null) {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Company not found"), HttpStatus.NOT_FOUND);
        }

        // check auth
        if (authKey.equals(foundCompany.getAuthKey())) {
            Team team = foundCompany.findTeamByName(teamName);
            if (team != null) {
                return new ResponseEntity<Team>(team, HttpStatus.OK);
            } else {
                return new ResponseEntity<ErrorMsg>(new ErrorMsg("Team not found"), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Wrong auth key"), HttpStatus.NOT_FOUND);
        }
    }

    private Company findCompanyByName(String companyName) {
        for (Company c : companyRepository.findAll()) {
            if (c.getName().equalsIgnoreCase(companyName)) {
                return c;
            }
        }
        return null;
    }
}

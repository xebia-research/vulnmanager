package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import com.xebia.vulnmanager.models.request.CompanyReq;
import com.xebia.vulnmanager.models.request.TeamReq;
import com.xebia.vulnmanager.services.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping(value = "/{company}")
public class CompanyController {
    private static final String IS_AUTHENTICATED_STRING = "isAuthenticated";
    private static final String AUTH_NOT_CORRECT_STRING = "Auth not correct!";

    private CompanyService companyService;

    @Autowired
    public CompanyController(final CompanyService companyService) {
        this.companyService = companyService;
    }

    /**
     * Get the teams of a specific company
     * @param authKey The auth key of the company used to auth the request
     * @param companyName The name of the company
     * @return A list of teams within the team
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getCompany(@RequestHeader(value = "auth", defaultValue = "nope") String authKey, @PathVariable("company") String companyName) {
        // find company by name
        Company foundCompany = companyService.getCompanyByName(companyName);

        if (foundCompany == null) {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Company not found"), HttpStatus.NOT_FOUND);
        }

        // check auth
        if (authKey.equals(foundCompany.getAuthKey())) {
            return new ResponseEntity<Company>(foundCompany, HttpStatus.OK);
        } else {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Wrong auth key"), HttpStatus.BAD_REQUEST);
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
        Company foundCompany = companyService.getCompanyByName(companyName);

        if (foundCompany == null) {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Company not found"), HttpStatus.NOT_FOUND);
        }

        // check auth
        if (authKey.equals(foundCompany.getAuthKey())) {
            Team team = companyService.getTeamOfCompany(companyName, teamName);
            if (team != null) {
                return new ResponseEntity<Team>(team, HttpStatus.OK);
            } else {
                return new ResponseEntity<ErrorMsg>(new ErrorMsg("Team not found"), HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Wrong auth key"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create a company by post request
     * @param company The request with information about the company
     * @return A response
     */
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> createCompany(@RequestBody CompanyReq company) {
        if (company.getName().length() < 1) {
            return new ResponseEntity<>(new ErrorMsg("Name to short"), HttpStatus.BAD_REQUEST);
        }

        Company found = companyService.getCompanyByName(company.getName());

        if (found != null) {
            return new ResponseEntity<>(new ErrorMsg("There is already a company with that name"), HttpStatus.BAD_REQUEST);
        }

        Company comp = new Company(company.getName());
        // Generate auth key when implemented


        // find company by name
        Company savedCompany = companyService.addCompany(comp);

        if (savedCompany == null) {
            return new ResponseEntity<ErrorMsg>(new ErrorMsg("Company not added"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(savedCompany, HttpStatus.OK);
    }


    /**
     * Add a team with a post request
     * @param teamReq A request with info about the team
     * @param companyName the company to wich the team needs to be added
     * @return Returns a response
     */
    @RequestMapping(value = "/{team}", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<?> creatTeam(@RequestBody TeamReq teamReq, @RequestHeader(value = "auth", defaultValue = "nope") String authKey, @PathVariable("company") String companyName) {
        // find company by name
        Company foundCompany = companyService.getCompanyByName(companyName);

        // check auth
        if (foundCompany == null || !authKey.equals(foundCompany.getAuthKey())) {
            return new ResponseEntity<>(new ErrorMsg("Company or auth incorrect"), HttpStatus.BAD_REQUEST);
        }

        if (teamReq.getName().length() < 1) {
            return new ResponseEntity<>(new ErrorMsg("Name to short"), HttpStatus.BAD_REQUEST);
        }

        Company found = companyService.getCompanyByName(companyName);

        if (found == null) {
            return new ResponseEntity<>(new ErrorMsg("Company not found"), HttpStatus.BAD_REQUEST);
        }

        for (Team t : found.getTeams()) {
            if (t.getName().equalsIgnoreCase(teamReq.getName())) {
                return new ResponseEntity<>(new ErrorMsg("Team with that name already exists"), HttpStatus.CONFLICT);
            }
        }

        Team newTeam = new Team(teamReq.getName());
        newTeam.setCompany(found);

        // add team
        companyService.addTeamCompany(companyName, newTeam);
        return new ResponseEntity<>(newTeam, HttpStatus.OK);
    }
}

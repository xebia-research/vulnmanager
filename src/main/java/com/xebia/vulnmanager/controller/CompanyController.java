package com.xebia.vulnmanager.controller;

import com.xebia.vulnmanager.auth.AuthenticationChecker;
import com.xebia.vulnmanager.data.MockCompanyFactory;
import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.models.net.ErrorMsg;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/{company}")
public class CompanyController {
    /**
     * Get the teams of a specific company
     * @param authKey The auth key of the company used to auth the request
     * @param companyName The name of the company
     * @return A list of teams within the team
     */
    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<?> getCompanyTeams(@RequestHeader(value = "auth", defaultValue = "nope") String authKey, @PathVariable("company") String companyName) {
        AuthenticationChecker authChecker = new AuthenticationChecker();
        boolean authIsGood = authChecker.checkCompanyAuthKey(companyName, authKey);
        MockCompanyFactory compFact = new MockCompanyFactory();

        if (authIsGood) {
            Company foundComp = compFact.findCompanyByName(companyName);
            return new ResponseEntity<Company>(foundComp, HttpStatus.OK);
        }

        return new ResponseEntity<ErrorMsg>(new ErrorMsg("Wrong auth key or company not found"), HttpStatus.NOT_FOUND);
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
        AuthenticationChecker authChecker = new AuthenticationChecker();
        boolean authIsGood = authChecker.checkTeamAndCompany(companyName, authKey, teamName);
        MockCompanyFactory compFact = new MockCompanyFactory();

        if (authIsGood) {
            Company foundComp = compFact.findCompanyByName(companyName);
            Team team = foundComp.findTeamByName(teamName);
            return new ResponseEntity<Team>(team, HttpStatus.OK);
        }

        return new ResponseEntity<ErrorMsg>(new ErrorMsg("Wrong auth key or company not found"), HttpStatus.NOT_FOUND);
    }
}

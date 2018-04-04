package com.xebia.vulnmanager.auth;

import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationChecker {

    @Autowired
    private CompanyRepository companyRepository;

    /**
     * Check if a company exists and if the auth key is right
     * @param companyName The name of the company to find
     * @param authKey The auth key of the company to check
     * @return Return false if the company doen't exists or the auth key is wrong
     */
    public boolean checkCompanyAuthKey(String companyName, String authKey) {
        Company getComp = null;
        if (companyRepository.findByName(companyName).size() > 0) {
            getComp = companyRepository.findByName(companyName).get(0);
        }

        if (getComp != null && getComp.getName().equals(companyName) && getComp.getAuthKey().equals(authKey)) {
               return true;
        }
        return false;
    }

    /**
     * Check if a team is within a company
     * @param company The company to check
     * @param teamName The name of the team to find
     * @return True if the team exists
     */
    public boolean checkIfTeamExists(Company company, String teamName) {
        if (company != null) {
            Team team = company.findTeamByName(teamName);
            if (team != null) {
               return true;
            }
        }
        return false;
    }

    public boolean checkTeamAndCompany(String company, String auth, String teamName) {
        if (checkCompanyAuthKey(company, auth)) {
            Company found = companyRepository.findByName(company).get(0);
            if (found.findTeamByName(teamName) != null) {
                return true;
            }
        }
        return false;
    }
}

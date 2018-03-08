package com.xebia.vulnmanager.auth;

import com.xebia.vulnmanager.data.MockCompanyFactory;
import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Team;

public class AuthenticationChecker {

    private MockCompanyFactory companies = new MockCompanyFactory();

    /**
     * Check if a company exists and if the auth key is right
     * @param companyName The name of the company to find
     * @param authKey The auth key of the company to check
     * @return Return false if the company doen't exists or the auth key is wrong
     */
    public boolean checkCompanyAuthKey(String companyName, String authKey) {
        Company getComp = companies.findCompanyByName(companyName);

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
            Company found = companies.findCompanyByName(company);
            if (found.findTeamByName(teamName) != null) {
                return true;
            }
        }
        return false;
    }
}

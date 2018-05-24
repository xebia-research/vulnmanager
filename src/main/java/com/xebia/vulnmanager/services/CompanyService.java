package com.xebia.vulnmanager.services;

import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Team;
import com.xebia.vulnmanager.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;

    @Autowired
    public CompanyService(final CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company getCompanyByName(String name) {
        Company foundCompany = null;
        if (companyRepository.findByName(name).size() > 0) {
            return companyRepository.findByName(name).get(0);
        }
        return null;
    }

    public List<Team> getTeamsOfCompany(String compName) {
        Company company = getCompanyByName(compName);
        if (company != null) {
            return company.getTeams();
        }
        return null;
    }

    public Team getTeamOfCompany(String companyName, String teamName) {
        List<Team> teams = getTeamsOfCompany(companyName);
        if (teams != null) {
            Optional<Team> foundTeam = teams.stream().filter(t -> t.getName().equals(teamName)).findFirst();
            if (foundTeam.isPresent()) {
                return foundTeam.get();
            }
        }
        return null;
    }

    public CompanyRepository getCompanyRepository() {
        return companyRepository;
    }
}

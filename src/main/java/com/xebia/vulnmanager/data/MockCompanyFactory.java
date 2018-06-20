package com.xebia.vulnmanager.data;

import com.xebia.vulnmanager.models.company.Company;
import com.xebia.vulnmanager.models.company.Person;
import com.xebia.vulnmanager.models.company.Team;

import java.util.Arrays;
import java.util.List;

public class MockCompanyFactory {
    private List<Company> companies;

    public MockCompanyFactory() {
        Person p1 = new Person("p1");
        p1.setId(1L);
        Person p2 = new Person("p2");
        p2.setId(2L);
        Person p3 = new Person("p3");
        p3.setId(3L);

        Team t1 = new Team("vulnmanager");
        Team t2 = new Team("headerbuddy");

        t1.addTeamMember(p1);
        t1.addTeamMember(p2);

        t2.addTeamMember(p2);
        t2.addTeamMember(p3);

        Company c1 = new Company("xebia");
        p1.setCompany(c1);
        p2.setCompany(c1);
        p3.setCompany(c1);

        c1.addTeam(t1);
        t1.setCompany(c1);
        c1.addTeam(t2);
        t2.setCompany(c1);

        companies = Arrays.asList(c1);
    }

    public List<Company> getMockCompanies() {
        return companies;
    }

    public Company findCompanyByName(String name) {
        for (Company comp : companies) {
            if (comp.getName().equals(name)) {
                return comp;
            }
        }
        return null;
    }

    public Team findTeamByName(String name, Company company) {
        for (Team team : company.getTeams()) {
            if (team.getName().equals(name)) {
                return team;
            }
        }
        return null;
    }
}

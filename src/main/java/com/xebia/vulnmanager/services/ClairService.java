package com.xebia.vulnmanager.services;

import com.xebia.vulnmanager.models.clair.objects.ClairReport;
import com.xebia.vulnmanager.repositories.ClairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClairService {

    private ClairRepository clairRepository;

    @Autowired
    public ClairService(final ClairRepository clairRepository) {
        this.clairRepository = clairRepository;
    }

    /**
     * Get all clair reports
     *
     * @return Return an iterable of all clair reports
     */
    public List<ClairReport> getAllReports() {
        return clairRepository.findAll();
    }

    public List<ClairReport> getAllReportsByTeam(String companyName, String teamName) {
        List<ClairReport> all = clairRepository.findAll();
        List<ClairReport> result = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            ClairReport report = all.get(i);

            if (report.getTeam().getName().equalsIgnoreCase(teamName)
                    && report.getTeam().getCompany().getName().equalsIgnoreCase(companyName)) {
                result.add(report);
            }
        }

        return result;
    }

    /**
     * Get a clair report by id
     *
     * @param id The id of the report
     * @return Return a report if it is present else it will return null
     */
    public ClairReport getReportById(long id) {
        return clairRepository.findById(id).get();
    }
}

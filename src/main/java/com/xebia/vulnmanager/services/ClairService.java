package com.xebia.vulnmanager.services;

import com.xebia.vulnmanager.models.clair.objects.ClairReport;
import com.xebia.vulnmanager.repositories.ClairRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /**
     * Get a clair report by id
     *
     * @param id The id of the report
     * @return Return a report if it is present else it will return null
     */
    public ClairReport getReportById(long id) {
        return clairRepository.findById(id).orElse(null);
    }
}

package com.xebia.vulnmanager.services;

import com.xebia.vulnmanager.models.generic.GenericReport;
import com.xebia.vulnmanager.models.generic.GenericResult;
import com.xebia.vulnmanager.repositories.GenericRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenericReportService {
    private GenericRepository genRepository;

    @Autowired
    public GenericReportService(final GenericRepository genRepository) {
        this.genRepository = genRepository;
    }


    public List<GenericReport> getAllReports() {
        return genRepository.findAll();
    }

    public Optional<GenericReport> getReportByGenericId(long id) {
        List<GenericReport> res = genRepository.findAll();
        Optional<GenericReport> returnReport = Optional.empty();
        for (GenericReport report : res) {
            if (report.getId() == id) {
                returnReport = Optional.of(report);
                break;
            }
        }
        return returnReport;
    }

    public Optional<GenericResult> getReportByGenericId(long reportId, long resultId) {
        List<GenericReport> res = genRepository.findAll();
        Optional<GenericResult> returnReport = Optional.empty();
        for (GenericReport report : res) {
            if (report.getId() == reportId) {
                for (GenericResult result : report.getGenericResults()) {
                    if (result.getId() == resultId) {
                        returnReport = Optional.of(result);
                        break;
                    }
                }
            }
        }
        return returnReport;
    }

    public GenericReport saveComment(GenericReport result) {
        return genRepository.save(result);
    }
}

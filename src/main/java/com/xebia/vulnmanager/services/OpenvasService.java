package com.xebia.vulnmanager.services;

import com.xebia.vulnmanager.models.generic.GenericMultiReport;
import com.xebia.vulnmanager.models.openvas.objects.OpenvasReport;
import com.xebia.vulnmanager.models.openvas.objects.OvResult;
import com.xebia.vulnmanager.repositories.OpenvasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OpenvasService {

    private OpenvasRepository openvasDataService;


    @Autowired
    public OpenvasService(final OpenvasRepository openvasDataService) {
        this.openvasDataService = openvasDataService;
    }

    /**
     * Get all the openvas reports
     * @return Return an iteratable of all the openvas reports
     */
    public List<OpenvasReport> getAllReports() {
        return openvasDataService.findAll();
    }

    /**
     * Get all the openvas reports
     * @return Return an iteratable of all the openvas reports
     */
    public GenericMultiReport getAllReportsAsGeneric() {
        List<OpenvasReport> reports = openvasDataService.findAll();
        GenericMultiReport genericReports = new GenericMultiReport();

        for (OpenvasReport report : reports) {
            genericReports.addReports(report.getGenericMultiReport().getReports().get(0));
        }
        return genericReports;
    }

    /**
     * Get an openvas report by id
     * @param id The id of the report
     * @return Return a report if it is present else it will return null
     */
    public OpenvasReport getReportById(long id) {
        return openvasDataService.findById(id).get();
    }

    /**
     * Get a result of an report
     * @param reportId The report id
     * @param resultId The result id
     * @return The result that matches the report. Returns null if not found
     */
    public OvResult getFromRaportByIdResultById(long reportId, long resultId) {
        OpenvasReport report = getReportById(reportId);
        if (report != null) {
            if (report.getResults().stream()
                    .noneMatch((e) -> e.getId().equals(resultId))) {
                return null;
            } else {
                return report.getResults().stream()
                        .filter((e) -> e.getId().equals(resultId))
                        .findFirst()
                        .get();
            }
        }
        return null;
    }

}

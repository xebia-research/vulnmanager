package com.xebia.vulnmanager.services;

import com.xebia.vulnmanager.models.generic.GenericMultiReport;
import com.xebia.vulnmanager.models.generic.GenericReport;
import com.xebia.vulnmanager.models.zap.objects.ScannedSiteInformation;
import com.xebia.vulnmanager.models.zap.objects.ZapReport;
import com.xebia.vulnmanager.repositories.OwaspZapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OwaspZapService {

    private OwaspZapRepository zapRepository;


    @Autowired
    public OwaspZapService(final OwaspZapRepository zapRepository) {
        this.zapRepository = zapRepository;
    }

    /**
     * Get all the Owasp Zap reports
     *
     * @return Return an iteratable of all the owasp zap reports
     */
    public List<ZapReport> getAllReports() {
        return zapRepository.findAll();
    }

    /**
     * Get an zap report by id
     *
     * @param id The id of the report
     * @return Return a report if it is present else it will return null
     */
    public ZapReport getReportById(long id) {
        return zapRepository.findById(id).get();
    }

    /**
     * Get a specific site from a Zap report.
     *
     * @param reportId  The id from the report the user wants to get the site from.
     * @param siteIndex The site the user wants to get from the report
     * @return ZapReport
     */
    public ScannedSiteInformation getSiteInformationFromReportById(long reportId, long siteIndex) {
        ZapReport zapReport = getReportById(reportId);
        if (zapReport == null) {
            return null;
        }

        List<ScannedSiteInformation> siteInformationList = zapReport.getScannedSitesInformation();
        for (ScannedSiteInformation siteInformation : siteInformationList) {
            if (siteInformation.getId().equals(siteIndex)) {
                return siteInformation;
            }
        }

        return null;
    }

    public GenericMultiReport getAllReportsAsGeneric() {
        GenericMultiReport multiReport = new GenericMultiReport();

        for (ZapReport report : getAllReports()) {
            GenericMultiReport report1 = report.getGenericReport();
            for (GenericReport finalReport : report1.getReports()) {
                multiReport.addReports(finalReport);
            }
        }

        return multiReport;
    }
}

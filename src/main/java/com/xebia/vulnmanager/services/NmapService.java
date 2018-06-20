package com.xebia.vulnmanager.services;

import com.xebia.vulnmanager.models.generic.GenericMultiReport;
import com.xebia.vulnmanager.models.generic.GenericReport;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.repositories.NMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NmapService {
    private NMapRepository nMapRepository;

    @Autowired
    public NmapService(final NMapRepository nMapRepository) {
        this.nMapRepository = nMapRepository;
    }


    public List<NMapReport> getAllReports() {
        return nMapRepository.findAll();
    }

    public NMapReport getReportById(long id) {
        return nMapRepository.findById(id).orElse(null);
    }

    public GenericMultiReport getAllReportsAsGeneric() {
        GenericMultiReport multiReport = new GenericMultiReport();

        for (NMapReport report : getAllReports()) {
            GenericMultiReport report1 = report.getMultiReport();
            for (GenericReport finalReport : report1.getReports()) {
                multiReport.addReports(finalReport);
            }
        }

        return multiReport;
    }
}

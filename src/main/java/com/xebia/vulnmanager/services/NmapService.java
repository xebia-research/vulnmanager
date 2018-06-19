package com.xebia.vulnmanager.services;

import com.xebia.vulnmanager.models.generic.GenericMultiReport;
import com.xebia.vulnmanager.models.generic.GenericReport;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import com.xebia.vulnmanager.repositories.NMapRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public List<NMapReport> getAllReportsByTeam(String companyName, String teamName) {
        List<NMapReport> all = nMapRepository.findAll();
        List<NMapReport> result = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            NMapReport report = all.get(i);

            if (report.getTeam().getName().equalsIgnoreCase(teamName)
                    && report.getTeam().getCompany().getName().equalsIgnoreCase(companyName)) {
                result.add(report);
            }
        }

        return result;
    }

    public Optional<NMapReport> getReportById(long id) {
        return nMapRepository.findById(id);
    }

    public GenericMultiReport getAllReportsAsGeneric(String companyName, String teamName) {
        GenericMultiReport multiReport = new GenericMultiReport();

        for (NMapReport report : getAllReportsByTeam(companyName, teamName)) {
            GenericMultiReport report1 = report.getMultiReport();
            for (GenericReport finalReport : report1.getReports()) {
                multiReport.addReports(finalReport);
            }
        }

        return multiReport;
    }
}

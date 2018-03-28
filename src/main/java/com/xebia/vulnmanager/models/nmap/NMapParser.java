package com.xebia.vulnmanager.models.nmap;

import com.xebia.vulnmanager.models.nmap.objects.Host;
import com.xebia.vulnmanager.models.nmap.objects.NMapGeneralInformation;
import com.xebia.vulnmanager.models.nmap.objects.NMapReport;
import org.w3c.dom.Document;

import java.util.List;


/**
 * The class NMapParser has functions for parsing a given document
 */

public class NMapParser {
    public NMapReport getNMapReport(Document nMapDoc) {
        ScanDataParserHelper scanDataParserHelper = new ScanDataParserHelper();
        HostsParserHelper hostsParserHelper = new HostsParserHelper();
        NMapReport nMapReport = new NMapReport();


        NMapGeneralInformation nMapScanData = scanDataParserHelper.getReportData(nMapDoc);
        List<Host> hosts = hostsParserHelper.getHostsFromDocument(nMapDoc);

        nMapReport.setScanData(nMapScanData);
        nMapReport.setHosts(hosts);

        addNMapReportToHosts(hosts, nMapReport);
        return nMapReport;
    }

    private void addNMapReportToHosts(List<Host> hosts, NMapReport nMapReport) {
        for (Host host : hosts) {
            host.setParentNMapReport(nMapReport);
        }
    }
}

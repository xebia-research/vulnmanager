package com.xebia.vulnmanager.nmap;

import com.xebia.vulnmanager.nmap.objects.Host;
import com.xebia.vulnmanager.nmap.objects.NMapGeneralInformation;
import com.xebia.vulnmanager.nmap.objects.NMapReport;
import org.w3c.dom.Document;

import java.util.List;


/**
 * The class NMapParser has functions for parsing a given document
 */

public class NMapParser {
    public static NMapReport parseNMapDocument(Document nMapDoc) {
        NMapGeneralInformation nMapScanData = ScanDataParserHelper.getReportData(nMapDoc);
        List<Host> hosts = HostsParserHelper.getHostsFromDocument(nMapDoc);
        return new NMapReport(nMapScanData, hosts);
    }
}

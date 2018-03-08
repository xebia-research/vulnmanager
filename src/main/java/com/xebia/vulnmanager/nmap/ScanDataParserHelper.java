package com.xebia.vulnmanager.nmap;

import com.xebia.vulnmanager.nmap.objects.NMapScanTask;
import com.xebia.vulnmanager.nmap.objects.NMapInfo;
import com.xebia.vulnmanager.nmap.objects.NMapScanInfo;
import com.xebia.vulnmanager.nmap.objects.GeneralScanResult;
import com.xebia.vulnmanager.nmap.objects.NMapGeneralInformation;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ScanDataParserHelper {
    public NMapGeneralInformation getReportData(Document nMapDoc) {
        NamedNodeMap reportData = nMapDoc.getElementsByTagName(NMapConstants.PARSER_LITERAL_NMAP_RUN).item(0).getAttributes();
        NMapInfo nMapInfo = getNMapInfo(reportData);

        NamedNodeMap scanInfo = nMapDoc.getElementsByTagName(NMapConstants.PARSER_LITERAL_NMAP_SCAN_INFO).item(0).getAttributes();
        NodeList tasksList = nMapDoc.getElementsByTagName(NMapConstants.PARSER_LITERAL_NMAP_TASK_END);
        NMapScanInfo nMapScanInfo = getNMapScanInfo(scanInfo, tasksList);

        NodeList runStatistics = nMapDoc.getElementsByTagName(NMapConstants.PARSER_LITERAL_NMAP_RUNSTATS).item(0).getChildNodes();
        GeneralScanResult generalScanResult = getGeneralScanResults(runStatistics);

        return new NMapGeneralInformation(nMapInfo, nMapScanInfo, generalScanResult);
    }

    private NMapInfo getNMapInfo(NamedNodeMap reportData) {
        String startDate = reportData.getNamedItem(NMapConstants.PARSER_LITERAL_START_TIMEDATE_STR).getNodeValue();
        LocalDateTime startScanDateTime = getLocalDateTimeFromString(startDate);

        String runArguments = reportData.getNamedItem(NMapConstants.PARSER_LITERAL_ARGS).getNodeValue();
        String nMapVersion = reportData.getNamedItem(NMapConstants.PARSER_LITERAL_VERSION).getNodeValue();
        return new NMapInfo(startScanDateTime, nMapVersion, runArguments);
    }

    private LocalDateTime getLocalDateTimeFromString(String dateTimeString) {
        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
                // Case insensitive to parse JAN and FEB
                .parseCaseInsensitive()
                // The pattern that is parsed
                .appendPattern("eee MMM dd HH:mm:ss yyyy")
                // Set Locale of formatter to English, if Locale is different from English,
                // the formatter may not understand what for example Jan as in January is
                .toFormatter(Locale.ENGLISH);
        return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
    }

    private NMapScanInfo getNMapScanInfo(NamedNodeMap scanInfo, NodeList tasksList) {
        String scanType = scanInfo.getNamedItem(NMapConstants.PARSER_LITERAL_TYPE).getNodeValue();
        String scanProtocol = scanInfo.getNamedItem(NMapConstants.PARSER_LITERAL_PROTOCOL).getNodeValue();
        String scanNumberOfServices = scanInfo.getNamedItem(NMapConstants.PARSER_LITERAL_NUM_SERVICES).getNodeValue();
        String scanServices = scanInfo.getNamedItem(NMapConstants.PARSER_LITERAL_SERVICES).getNodeValue();

        List<NMapScanTask> scanTasks = getScanTaskList(tasksList);

        return new NMapScanInfo(scanType, scanProtocol, scanNumberOfServices, scanServices, scanTasks);
    }

    private List<NMapScanTask> getScanTaskList(NodeList tasksList) {
        List<NMapScanTask> scanTasks = new ArrayList<>();

        for (int x = 0; x < tasksList.getLength(); x++) {
            NamedNodeMap taskInformation = tasksList.item(x).getAttributes();
            String taskName = taskInformation.getNamedItem(NMapConstants.PARSER_LITERAL_NMAP_TASK).getNodeValue();
            String extraInfo = null;

            Node extraInfoNode = taskInformation.getNamedItem(NMapConstants.PARSER_LITERAL_NMAP_EXTRA_INFO);
            if (extraInfoNode != null) {
                extraInfo = extraInfoNode.getNodeValue();
            }
            scanTasks.add(new NMapScanTask(taskName, extraInfo));
        }
        return scanTasks;
    }

    private GeneralScanResult getGeneralScanResults(NodeList runStatistics) {
        String numberOfScannedHosts = null, numberOfHostUp = null, numberOfHostDown = null, timeElapsed = null;

        for (int x = 0; x < runStatistics.getLength(); x++) {
            Node currentStatisticNode = runStatistics.item(x);
            String statisticName = currentStatisticNode.getNodeName();
            if (statisticName.equals(NMapConstants.PARSER_LITERAL_NMAP_HOSTS)) {
                numberOfScannedHosts = currentStatisticNode.getAttributes().getNamedItem(NMapConstants.PARSER_LITERAL_NMAP_TOTAL).getNodeValue();
                numberOfHostUp = currentStatisticNode.getAttributes().getNamedItem(NMapConstants.PARSER_LITERAL_NMAP_UP).getNodeValue();
                numberOfHostDown = currentStatisticNode.getAttributes().getNamedItem(NMapConstants.PARSER_LITERAL_NMAP_DOWN).getNodeValue();
            } else if (statisticName.equals(NMapConstants.PARSER_LITERAL_NMAP_FINISHED)) {
                timeElapsed = currentStatisticNode.getAttributes().getNamedItem(NMapConstants.PARSER_LITERAL_NMAP_ELAPSED).getNodeValue();
            }
        }
        return new GeneralScanResult(numberOfScannedHosts, numberOfHostUp, numberOfHostDown, timeElapsed);
    }
}

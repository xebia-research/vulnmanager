import {Component, OnInit} from '@angular/core';
import {VulnApiService} from "../services/vuln-api.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-nmap-select-report',
  templateUrl: './nmap-select-report.component.html',
  styleUrls: ['./nmap-select-report.component.css']
})
export class NmapSelectReportComponent implements OnInit {

  nmapObjects: any;
  errorMessages: any;
  nmapObjectIsEmpty: boolean = true;

  constructor(private http: HttpClient, private apiService: VulnApiService) {
  }

  ngOnInit() {
    this.apiService.getNmap("xebia", "vulnmanager").subscribe(
      nmapReportData => {// data bestaat
        console.log(nmapReportData);
        this.nmapObjects = nmapReportData;

        if (Object.keys(nmapReportData).length === 0) {
          this.showError("There are no nmap reports, upload a report first!");
          this.nmapObjectIsEmpty = true;
        } else {
          this.nmapObjectIsEmpty = false;
        }
      },
      error => {
        this.showError("Could not get nmap reports: The following Http status code was given: " + error.status + ", with the text: " + error.statusText);
        this.nmapObjectIsEmpty = true;
      });
  }

  showError(msg) {
    this.errorMessages = [];
    this.errorMessages.push({severity: 'error', summary: 'Error Message:', detail: msg});
  }

}

import {Component, OnInit} from '@angular/core';
import {VulnApiService} from '../services/vuln-api.service';
import {HttpClient} from "@angular/common/http";


@Component({
  selector: 'app-openvas-select-report',
  templateUrl: './openvas-select-report.component.html',
  styleUrls: ['./openvas-select-report.component.css']
})
export class OpenvasSelectReportComponent implements OnInit {

  openVasObjects: any;
  errorMessages: any;
  openvasObjectIsEmpty: boolean = true;

  constructor(private http: HttpClient, private apiService: VulnApiService) {
  }

  ngOnInit() {
    this.apiService.getOpenvas("xebia", "vulnmanager").subscribe(
      openvasReportData => {// data bestaat
        console.log(openvasReportData);
        this.openVasObjects = openvasReportData;

        if (Object.keys(openvasReportData).length === 0) {
          this.showError("There are no openvas reports, upload a report first!");
          this.openvasObjectIsEmpty = true;
        } else {
          this.openvasObjectIsEmpty = false;
        }
      },
      error => {
        this.showError("Could not get openvas reports: The following Http status code was given: " + error.status + ", with the text: " + error.statusText);
        this.openvasObjectIsEmpty = true;
      });
  }

  showError(msg) {
    this.errorMessages = [];
    this.errorMessages.push({severity: 'error', summary: 'Error Message:', detail: msg});
  }

}

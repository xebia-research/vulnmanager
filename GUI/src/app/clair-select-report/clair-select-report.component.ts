import {Component, OnInit} from '@angular/core';
import {VulnApiService} from "../services/vuln-api.service";
import {HttpClient} from "@angular/common/http";

@Component({
  selector: 'app-clair-select-report',
  templateUrl: './clair-select-report.component.html',
  styleUrls: ['./clair-select-report.component.css']
})
export class ClairSelectReportComponent implements OnInit {

  clairObjects: any;
  errorMessages: any;
  clairObjectIsEmpty: boolean;

  constructor(private http: HttpClient, private apiService: VulnApiService) {
  }

  ngOnInit() {
    this.apiService.getClair("xebia", "vulnmanager").subscribe(
      clairData => {// data bestaat
        console.log(clairData);
        this.clairObjects = clairData;

        if (Object.keys(clairData).length === 0) {
          this.showError("There are no clair reports, upload a report first!");
          this.clairObjectIsEmpty = true;
        } else {
          this.clairObjectIsEmpty = false;
        }
      },
      error => {
        this.showError("The following Http status code was given: " + error.status + ", with the text: " + error.statusText);
      });
  }

  showError(msg) {
    this.errorMessages = [];
    this.errorMessages.push({severity: 'error', summary: 'Error Message:', detail: msg});
  }

}

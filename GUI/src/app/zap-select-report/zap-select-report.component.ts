import {Component, OnInit} from '@angular/core';
import {VulnApiService} from "../services/vuln-api.service";
import {HttpClient} from "@angular/common/http";
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-zap-select-report',
  templateUrl: './zap-select-report.component.html',
  styleUrls: ['./zap-select-report.component.css']
})
export class ZapSelectReportComponent implements OnInit {

  zapObjects: any;
  errorMessages: any;
  zapObjectIsEmpty: boolean = true;

  constructor(private http: HttpClient, private apiService: VulnApiService, private router:Router) {
  }

  ngOnInit() {
    this.loadData();

    this.router.events.subscribe((event => {
      if(event instanceof NavigationEnd) {
        this.errorMessages = [];
        this.loadData();
      }
    }))
  }

  loadData() {
    this.apiService.getZap().subscribe(
      zapReportData => {// data bestaat
        console.log(zapReportData);
        this.zapObjects = zapReportData;

        if (Object.keys(zapReportData).length === 0) {
          this.showError("There are no zap reports, upload a report first!");
          this.zapObjectIsEmpty = true;
        } else {
          this.zapObjectIsEmpty = false;
        }
      },
      error => {
        this.showError("Could not get zap reports: The following Http status code was given: " + error.status + ", with the text: " + error.statusText);
        this.zapObjectIsEmpty = true;
      });
  }

  showError(msg) {
    this.errorMessages = [];
    this.errorMessages.push({severity: 'error', summary: 'Error Message:', detail: msg});
  }

}

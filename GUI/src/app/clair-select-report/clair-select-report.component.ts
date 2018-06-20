import {Component, OnInit} from '@angular/core';
import {VulnApiService} from "../services/vuln-api.service";
import {HttpClient} from "@angular/common/http";
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-clair-select-report',
  templateUrl: './clair-select-report.component.html',
  styleUrls: ['./clair-select-report.component.css']
})
export class ClairSelectReportComponent implements OnInit {

  clairObjects: any;
  errorMessages: any;
  clairObjectIsEmpty: boolean = true;

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
    this.apiService.getClair().subscribe(
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
        this.showError("Could not get clair reports: The following Http status code was given: " + error.status + ", with the text: " + error.statusText);
        this.clairObjectIsEmpty = true;
      });
  }

  showError(msg) {
    this.errorMessages = [];
    this.errorMessages.push({severity: 'error', summary: 'Error Message:', detail: msg});
  }

}

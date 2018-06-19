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

  constructor(private http: HttpClient, private apiService: VulnApiService, private router:Router) {
  }

  ngOnInit() {
    this.loadData();

    this.router.events.subscribe((event => {
      if(event instanceof NavigationEnd) {
        this.loadData();
      }
    }))
  }

  loadData() {
    this.apiService.getClair().subscribe((data) => {
      // data bestaat
      console.log(data);
      this.clairObjects = data;
    });
  }

}

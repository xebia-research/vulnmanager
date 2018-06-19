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
    this.apiService.getZap().subscribe((data) => {
      // data bestaat
      console.log(data);
      this.zapObjects = data;
    });
  }

}

import {Component, OnInit} from '@angular/core';
import {VulnApiService} from "../services/vuln-api.service";
import {HttpClient} from "@angular/common/http";
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-nmap-select-report',
  templateUrl: './nmap-select-report.component.html',
  styleUrls: ['./nmap-select-report.component.css']
})
export class NmapSelectReportComponent implements OnInit {

  nmapObjects: any;

  constructor(private http: HttpClient, private apiService: VulnApiService, private router:Router) {
  }

  ngOnInit() {
    this.apiService.getNmap().subscribe((data) => {
      // data bestaat
      console.log(data);
      this.nmapObjects = data;
    });

    this.router.events.subscribe((event => {
      if(event instanceof NavigationEnd) {
        this.apiService.getNmap().subscribe((data) => {
          // data bestaat
          console.log(data);
          this.nmapObjects = data;
        });
      }
    }))
  }
}

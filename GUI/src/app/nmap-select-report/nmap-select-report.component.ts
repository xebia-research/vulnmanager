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

  constructor(private http: HttpClient, private apiService: VulnApiService) {
  }

  ngOnInit() {
    this.apiService.getNmap("xebia", "vulnmanager").subscribe((data) => {
      // data bestaat
      console.log(data);
      this.nmapObjects = data;
    });
  }

}

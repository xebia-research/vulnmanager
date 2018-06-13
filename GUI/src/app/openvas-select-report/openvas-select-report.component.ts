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

  constructor(private http: HttpClient, private apiService: VulnApiService) {
  }

  ngOnInit() {
    this.apiService.getOpenvas("xebia", "vulnmanager").subscribe((data) => {
      // data bestaat
      console.log(data);
      this.openVasObjects = data;
    });
  }
}

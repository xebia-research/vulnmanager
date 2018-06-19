import {Component, OnInit} from '@angular/core';
import {VulnApiService} from '../services/vuln-api.service';
import {HttpClient} from "@angular/common/http";
import {NavigationEnd, Router} from "@angular/router";


@Component({
  selector: 'app-openvas-select-report',
  templateUrl: './openvas-select-report.component.html',
  styleUrls: ['./openvas-select-report.component.css']
})
export class OpenvasSelectReportComponent implements OnInit {

  openVasObjects: any;

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

  loadData(){
    this.apiService.getOpenvas().subscribe((data) => {
      // data bestaat
      console.log(data);
      this.openVasObjects = data;
    });
  }
}

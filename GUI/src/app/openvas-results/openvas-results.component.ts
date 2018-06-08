import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { MenuItem } from "primeng/api";
import { VulnApiService } from '../services/vuln-api.service';

@Component({
  selector: 'app-openvas-results',
  templateUrl: './openvas-results.component.html',
  styleUrls: ['./openvas-results.component.css'],
  providers: [VulnApiService]
})
export class OpenvasResultsComponent implements OnInit {
  openVasObject: any ;
  selectedOpenvas: any;
  displayDialog: boolean;
  tags:boolean;
  items: MenuItem[];

  constructor(private http: HttpClient, private apiService:VulnApiService) {
    this.apiService.addTest().subscribe(()=>{});
  }

  ngOnInit() {
    this.apiService.getOpenvas("xebia", "vulnmanager").subscribe((data) => {
      // data bestaat
      console.log(data) ;
      this.openVasObject = data[0];
    });
    // Dropdown for option button in p-header
    this.items = [
      {label: 'View scan info', icon: 'fa-eye', command: () => {

        }},
      {label: 'Delete', icon: 'fa-close', command: () => {

        }}
    ];
  }

  selectOpenvas(event: Event, selectedOpenvas: any) {
    this.selectedOpenvas = selectedOpenvas;
    this.displayDialog = true;
    this.tags = false;
    event.preventDefault();
  }
  onDialogHide() {
    this.selectedOpenvas = null;

  }
}

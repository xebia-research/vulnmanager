import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MenuItem} from "primeng/api";
import {VulnApiService} from "../services/vuln-api.service";

@Component({
  selector: 'app-zap-results',
  templateUrl: './zap-results.component.html',
  styleUrls: ['./zap-results.component.css']
})
export class ZapResultsComponent implements OnInit {
  zapObject: any ;
  selectedZap: any;
  displayDialog: boolean;
  items: MenuItem[];

  constructor(private http: HttpClient, private apiService:VulnApiService) {
    this.apiService.addTest().subscribe(()=>{});

  }

  ngOnInit() {
    this.apiService.getZap("xebia", "vulnmanager").subscribe((data) => {
      // data bestaat
      console.log(data) ;
      this.zapObject = data[0];
    });
    // Dropdown for option button in p-header
    this.items = [
      {label: 'View scan info', icon: 'fa-eye', command: () => {

        }},
      {label: 'Delete', icon: 'fa-close', command: () => {

        }}
    ];
  }
  selectZap(event: Event, selectedZAP: any) {
    this.selectedZap = selectedZAP;
    this.displayDialog = true;
    event.preventDefault();
  }
  onDialogHide() {
    this.selectedZap = null;

  }
  }


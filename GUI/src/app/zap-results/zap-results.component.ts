import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MenuItem} from "primeng/api";

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

  constructor(private http: HttpClient) {
    this.http.get('http://localhost:8080/addtest').subscribe(()=> {});

  }
  httpGetZap() {
    const httpOption = {
      headers: new HttpHeaders({
        'auth': 'testauth'
        //  todo is to implement JWT

      })
    };
    console.log(httpOption);
    return this.http.get('http://localhost:8080/xebia/vulnmanager/zap', httpOption) ;
  }
  ngOnInit() {
    this.httpGetZap().subscribe((data) => {
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


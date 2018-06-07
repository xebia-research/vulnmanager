import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MenuItem, SelectItem} from "primeng/api";

@Component({
  selector: 'app-openvas-results',
  templateUrl: './openvas-results.component.html',
  styleUrls: ['./openvas-results.component.css']
})
export class OpenvasResultsComponent implements OnInit {
  openVasObject: any ;
  selectedOpenvas: any;
  displayDialog: boolean;
  tags:boolean;
  items: MenuItem[];

  // Sort variables
  sortOptions: SelectItem[];
  sortKey: string;
  sortField: string;
  sortOrder: number;

  constructor(private http: HttpClient) {
    this.http.get('http://localhost:8080/addtest').subscribe(()=> {});
  }

  httpGetOpenVas() {
    const httpOption = {
      headers: new HttpHeaders({
        'auth': 'testauth'
        //  todo is to implement JWT

      })
    };
    console.log(httpOption);
    return this.http.get('http://localhost:8080/xebia/vulnmanager/openvas', httpOption) ;
  }
  ngOnInit() {
    this.httpGetOpenVas().subscribe((data) => {
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
    // Sort options
    this.sortOptions = [
      {label: 'Severity High - Low', value: '!severity'},
      {label: 'Severity Low - High', value: 'severity'}
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
  onSortChange(event) {
    let value = event.value;

    if (value.indexOf('!') === 0) {
      this.sortOrder = -1;
      this.sortField = value.substring(1, value.length);
    } else {
      this.sortOrder = 1;
      this.sortField = value;
    }
  }
}

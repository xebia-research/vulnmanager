import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {animate, state, style, transition, trigger} from '@angular/animations';
import { VulnApiService } from '../services/vuln-api.service';
import {MenuItem, SelectItem} from "primeng/api";

@Component({
  selector: 'app-nmap-results',
  templateUrl: './nmap-results.component.html',
  styleUrls: ['./nmap-results.component.css'],
  providers: [VulnApiService]
})
export class NmapResultsComponent implements OnInit {

  nMapObject: any;
  selectedNmapHost: any;
  displayDialog: boolean;
  items: MenuItem[];
  sortField: string;
  sortOrder: number;

  // Sort variables
  sortOptions: SelectItem[];
  sortKey: string;



  constructor(private http: HttpClient, private apiService:VulnApiService) {
    this.apiService.addTest().subscribe(()=>{});
  }

  ngOnInit() {
    this.apiService.getNmap("xebia", "vulnmanager").subscribe((data) => {
      // data bestaat
      console.log(data) ;
      this.nMapObject = data[0];
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
      {label: 'Hosts state (Descending)', value: '!stateDetails.state'},
      {label: 'Hosts state (Ascending)', value: 'stateDetails.state'},
      {label: 'Open ports (Descending)', value: '!hostPorts.ports.length'},
      {label: 'Open ports (Ascending)', value: 'hostPorts.ports.length'}
    ];

  }
  selectNmapHost(event: Event, selectedNmapHost: any) {
    this.selectedNmapHost = selectedNmapHost;
    this.displayDialog = true;
    event.preventDefault();
  }
  onDialogHide() {
    this.selectedNmapHost = null;
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

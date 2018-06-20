import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {VulnApiService} from '../services/vuln-api.service';
import {MenuItem, SelectItem} from "primeng/api";
import {ActivatedRoute} from "@angular/router";

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

  errorMessages: any;

  constructor(private http: HttpClient, private apiService: VulnApiService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      let reportId = params['id']; // (+) converts string 'id' to a number

      if (parseInt(reportId, 10)) {
        this.apiService.getNmapReport(reportId).subscribe((nmapObject) => {
          this.nMapObject = nmapObject;
        }, error => {
            this.showError("The following message was given: " + error.error.msg + ".This was for the report with id: " + reportId);
          });
      }
    });

    // Dropdown for option button in p-header
    this.items = [
      {
        label: 'View scan info', icon: 'fa-eye', command: () => {

        }
      },
      {
        label: 'Delete', icon: 'fa-close', command: () => {

        }
      }
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

  showError(msg) {
    this.errorMessages = [];
    this.errorMessages.push({severity: 'error', summary: 'Error Message:', detail: msg});
  }
}

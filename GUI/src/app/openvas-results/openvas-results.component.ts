import {Component, OnInit} from '@angular/core';
import {VulnApiService} from '../services/vuln-api.service';
import {HttpClient} from "@angular/common/http";
import {MenuItem, SelectItem} from "primeng/api";
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-openvas-results',
  templateUrl: './openvas-results.component.html',
  styleUrls: ['./openvas-results.component.css'],
  providers: [VulnApiService]
})
export class OpenvasResultsComponent implements OnInit {
  openVasObject: any;
  selectedOpenvas: any;
  displayDialog: boolean;
  tags: boolean;
  items: MenuItem[];

  sortOptions: SelectItem[];
  sortKey: string;
  sortField: string;
  sortOrder: number;

  errorMessages: any;

  constructor(private http: HttpClient, private apiService: VulnApiService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      let reportId = params['id']; // (+) converts string 'id' to a number

      if (parseInt(reportId, 10)) {
        this.apiService.getOpenvasReport("xebia", "vulnmanager", reportId).subscribe(
          openVasObject => {
            // data bestaat
            console.log(openVasObject);
            this.openVasObject = openVasObject;
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
      {label: 'Severity (Descending)', value: '!severity'},
      {label: 'Severity (Ascending)', value: 'severity'},
      {label: 'Port (Descending)', value: '!port'},
      {label: 'Port (Ascending)', value: 'port'}
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

  showError(msg) {
    this.errorMessages = [];
    this.errorMessages.push({severity: 'error', summary: 'Error Message:', detail: msg});
  }
}

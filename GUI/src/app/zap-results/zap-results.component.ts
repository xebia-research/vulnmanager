import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {MenuItem, SelectItem} from "primeng/api";
import {VulnApiService} from "../services/vuln-api.service";
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-zap-results',
  templateUrl: './zap-results.component.html',
  styleUrls: ['./zap-results.component.css']
})

export class ZapResultsComponent implements OnInit {
  zapObject: any;
  selectedZap: any;
  currentSiteInformation: any;
  displayDialog: boolean;
  items: MenuItem[];

  // Sort variables
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
        this.apiService.getReportZap(reportId).subscribe((zapObject) => {
          this.zapObject = zapObject;
          let scannedSites = this.zapObject.scannedSitesInformation;

            this.currentSiteInformation = scannedSites[0];
            this.setItems(scannedSites);

            // Sort options
            this.sortOptions = [
              {label: 'Severity High - Low', value: '!riskCode'},
              {label: 'Severity Low - High', value: 'riskCode'}
            ];
          }, error => {
            this.showError("The following message was given: " + error.error.msg + ".This was for the report with id: " + reportId);
          });
      }
    });
  }

  setItems(scannedSiteInformation: any) {
    let selectSiteItems = [];

    for (let i = 0; i < scannedSiteInformation.length; i++) {
      selectSiteItems.push({
        label: scannedSiteInformation[i].host, icon: 'fa-eye', command: () => {
          this.currentSiteInformation = scannedSiteInformation[i];
        }
      });
    }

    // Dropdown for option button in p-header
    this.items = selectSiteItems;
  }

  selectZap(event: Event, selectedZAP: any) {
    this.selectedZap = selectedZAP;
    this.displayDialog = true;
    event.preventDefault();
  }

  onDialogHide() {
    this.selectedZap = null;

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

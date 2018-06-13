import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MenuItem, SelectItem} from "primeng/api";
import {VulnApiService} from "../services/vuln-api.service";
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-clair-results',
  templateUrl: './clair-results.component.html',
  styleUrls: ['./clair-results.component.css']
})
export class ClairResultsComponent implements OnInit {

  clairObject: any;
  selectedClair: any;
  displayDialog: boolean;
  tags: boolean;
  items: MenuItem[];

  // Sort variables
  sortOptions: SelectItem[];
  sortKey: string;
  sortField: string;
  sortOrder: number;


  constructor(private http: HttpClient, private apiService: VulnApiService, private route: ActivatedRoute) {
  }

  ngOnInit() {
    this.route.params.subscribe(params => {
      let reportId = params['id']; // (+) converts string 'id' to a number

      if (parseInt(reportId, 10)) {
        this.apiService.getReportClair("xebia", "vulnmanager", reportId).subscribe((clairObject) => {
          this.clairObject = clairObject;
          // Add severity number so we can sort on this
          this.clairObject.vulnerabilities.forEach(function (vulnerability) {
            let severityNumber = ClairResultsComponent.getSeverityNumber(vulnerability.severity);
            vulnerability.severityNumber = severityNumber;
          });
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
      {label: 'Severity High - Low', value: '!severityNumber'},
      {label: 'Severity Low - High', value: 'severityNumber'}
    ];
  }

  static getSeverityNumber = function (priority) {
    return priority === 'Defcon1' ? 7
      : priority === 'Critical' ? 6
        : priority === 'High' ? 5
          : priority === 'Medium' ? 4
            : priority === 'Low' ? 3
              : priority === 'Negligible' ? 2
                : 1 // Unknown
  };

  selectClair(event: Event, selectedClair: any) {
    this.selectedClair = selectedClair;
    this.displayDialog = true;
    this.tags = false;
    event.preventDefault();
  }

  onDialogHide() {
    this.selectedClair = null;
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

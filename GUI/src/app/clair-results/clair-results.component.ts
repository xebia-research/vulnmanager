import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MenuItem, SelectItem} from "primeng/api";

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


  constructor(private http: HttpClient) {
    this.http.get('http://localhost:8080/addtest').subscribe(() => {
    });
  }

  ngOnInit() {
    this.httpGetClair().subscribe((data) => {
      // Add severity number so we can sort on this
      data[0].vulnerabilities.forEach(function (vulnerability) {
        let severityNumber = ClairResultsComponent.getSeverityNumber(vulnerability.severity);
        vulnerability.severityNumber = severityNumber;
      });

      this.clairObject = data[0];
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

  httpGetClair() {
    const httpOption = {
      headers: new HttpHeaders({
        'auth': 'testauth'
        //  todo is to implement JWT

      })
    };
    return this.http.get('http://localhost:8080/xebia/vulnmanager/clair', httpOption);
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

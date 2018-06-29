import {Component, OnInit} from '@angular/core';
import {MenuItem, SelectItem} from "primeng/api";
import {HttpClient} from "@angular/common/http";
import {VulnApiService} from "../services/vuln-api.service";
import {NgForm} from "@angular/forms";
import {CheckboxModule} from 'primeng/checkbox';
import {NavigationEnd, Router} from "@angular/router";


@Component({
  selector: 'app-generic-results',
  templateUrl: './generic-results.component.html',
  styleUrls: ['./generic-results.component.css'],
  styles: [`
        .custombar1 .ui-scrollpanel-wrapper {
            border-right: 9px solid #f4f4f4;
        }
            
        .custombar1 .ui-scrollpanel-bar {
            background-color: #1976d2;
            opacity: 1;
            transition: background-color .3s;
        }
            
        .custombar1 .ui-scrollpanel-bar:hover {
            background-color: #135ba1;
        }
    `],
})
export class GenericResultsComponent implements OnInit {
  genericReports: any;
  selectedReport: any;
  genericReportIsEmpty: boolean = true;

  displayDialog: boolean;
  items: MenuItem[];
  sortField: string;
  sortOrder: number;
  text: String;
  errorMessages: any;

  // Sort variables
  sortOptions: SelectItem[];
  sortKey: string;


  constructor(private http: HttpClient, private apiService:VulnApiService, private router:Router) {
    //this.apiService.addTest().subscribe(()=>{});
  }


  ngOnInit() {

    this.loadData();
    this.router.events.subscribe(
      value => {
        if (value instanceof NavigationEnd) {

          this.loadData();
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
      {label: 'Result amount (Descending)', value: '!genericResults.length'},
      {label: 'Result amount (Ascending)', value: 'genericResults.length'},
      {label: 'Open ports (Descending)', value: '!hostPorts.ports.length'},
      {label: 'Open ports (Ascending)', value: 'hostPorts.ports.length'}
    ];
  }

  loadData() {
    this.apiService.getGenericMulti().subscribe(
      genericReportData => {// data bestaat
        console.log(genericReportData);
        this.genericReports = genericReportData;

        if (Object.keys(genericReportData).length === 0) {
          this.showError("There are no generic reports, upload a report first!");
          this.genericReportIsEmpty = true;
        } else {
          this.genericReportIsEmpty = false;
        }
      },
      error => {
        this.showError("Could not get generic reports: The following Http status code was given: " + error.status + ", with the text: " + error.statusText);
        this.genericReportIsEmpty = true;
      });
  }

  selectNmapHost(event: Event, selectedReport: any) {
    this.selectedReport = selectedReport;
    this.displayDialog = true;
    event.preventDefault();
  }

  onDialogHide() {
    this.selectedReport = null;
  }

  formSubmit(form, reportId, resultId) {
    console.log(reportId, resultId);
    console.log(form.value.text);
    if(form.valid) {
      this.apiService.postComment(reportId, resultId, form.value.text).subscribe(() =>{
        this.genericReports.forEach((report) => {
          if (report.id == reportId) {
            report.genericResults.forEach((result) => {
              if (result.id == resultId) {
                let comment: any = {};
                comment.user = {};
                comment.user.username = localStorage.getItem("user");
                comment.content = form.value.text;
                result.comments.unshift(comment);
                this.text = '';
              }
            })
          }
        })
      });
    }
  }

  changeFalsePositive(reportId, resultId, falsePositive) {
    this.apiService.postFalsePositive(reportId, resultId).subscribe(() =>{
      this.genericReports.forEach((report) => {
        if (report.id == reportId) {
          report.genericResults.forEach((result) => {
            if (result.id == resultId) {
              result.falsePositive = !falsePositive;
            }
          })
        }
      })
    });
  }

  onSubmit(f: NgForm) {
    console.log(f.value)
    console.log(f.value.text);  // { first: '', last: '' }
    console.log(f.valid);  // false
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

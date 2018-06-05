import {Component, OnInit} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {MenuItem} from "primeng/api";

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


  constructor(private http: HttpClient) {
    this.http.get('http://localhost:8080/addtest').subscribe(() => {
    });
  }

  ngOnInit() {
    this.httpGetClair().subscribe((data) => {
      // data bestaat
      console.log(data);
      this.clairObject = data[0].genericReport.reports[0].genericResults;
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
  }

  httpGetClair() {
    const httpOption = {
      headers: new HttpHeaders({
        'auth': 'testauth'
        //  todo is to implement JWT

      })
    };
    console.log(httpOption);
    return this.http.get('http://localhost:8080/xebia/vulnmanager/clair', httpOption);
  }

  selectClair(event: Event, selectedClair: any) {
    this.selectedClair = selectedClair;
    this.displayDialog = true;
    this.tags = false;
    event.preventDefault();
  }

  onDialogHide() {
    this.selectedClair = null;

  }

}

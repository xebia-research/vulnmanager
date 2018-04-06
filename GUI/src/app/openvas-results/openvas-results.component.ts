import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Component({
  selector: 'app-openvas-results',
  templateUrl: './openvas-results.component.html',
  styleUrls: ['./openvas-results.component.css']
})
export class OpenvasResultsComponent implements OnInit {
  openVasObject: any ;
  selectedOpenvas: any;
  displayDialog: boolean;


  constructor(private http: HttpClient) {
    this.http.get('http://localhost:8080/addtest').subscribe(()=> {});
  }

  httpGetOpenVas() {
    const httpOption = {
      headers: new HttpHeaders({
        'auth': 'testauth'
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
  }

  selectOpenvas(event: Event, selectedOpenvas: any) {
    this.selectedOpenvas = selectedOpenvas;
    this.displayDialog = true;
    event.preventDefault();
  }
  onDialogHide() {
    this.selectedOpenvas = null;
  }
}

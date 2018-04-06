import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {animate, state, style, transition, trigger} from '@angular/animations';

@Component({
  selector: 'app-nmap-results',
  templateUrl: './nmap-results.component.html',
  styleUrls: ['./nmap-results.component.css'],
})
export class NmapResultsComponent implements OnInit {

  nMapObject: any;
  selectedNmapHost: any;
  displayDialog: boolean;

  constructor(private http: HttpClient) {
    this.http.get('http://localhost:8080/addtest').subscribe(()=> {
    });
  }
  httpGetNmap() {
    const httpOption = {
      headers: new HttpHeaders({
        'auth': 'testauth'
      })
    };
    return this.http.get('http://localhost:8080/xebia/vulnmanager/nmap', httpOption);
  }

  ngOnInit() {
    this.httpGetNmap().subscribe((data) => {
      // data bestaat
      console.log(data) ;
      this.nMapObject = data[0];
    });
  }
  selectNmapHost(event: Event, selectedNmapHost: any) {
    this.selectedNmapHost = selectedNmapHost;
    this.displayDialog = true;
    event.preventDefault();
  }
  onDialogHide() {
    this.selectedNmapHost = null;
  }

}

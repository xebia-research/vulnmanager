import { Component, OnInit } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css']
})
export class ResultsComponent implements OnInit {
  title = 'app';
  showOpenVas: boolean;
  showNmap: boolean;
  openVasObject: any ;
  nMapObject: any;

  selectedOpenvas: any;
  selectedNmapHost: any;
  displayDialog: boolean;

  constructor(private http: HttpClient) { }
  greetings(person: string) {
    this.title = 'Hello mr ' + person;
    this.showOpenVas = false;
    this.showNmap = false;
  }
  httpGetOpenVas() {
    const httpOption = {
      headers: new HttpHeaders({
        'auth': 'testauth'
      })
    };

    // Bij functies in de opcontroller of nmpacontroller voeg
    //  @CrossOrigin(origins = "http://localhost:4200") toe

    // headers = headers.append();
    // headers.set('auth', 'testauth');
    console.log(httpOption);
    return this.http.get('http://localhost:8080/xebia/vulnmanager/openvas', httpOption) ;
  }

  httpGetNmap() {
    const httpOption = {
      headers: new HttpHeaders({
        'auth': 'testauth'
      })
    };
    return this.http.get('http://localhost:8080/xebia/vulnmanager/nmap', httpOption) ;

  }
  ngOnInit() {
    this.greetings('Pieter');
    this.httpGetOpenVas().subscribe((data) => {
      // data bestaat
      console.log(data) ;
      this.openVasObject = data;
      });

    this.httpGetNmap().subscribe((data) => {
      // data bestaat
      console.log(data) ;
      this.nMapObject = data ;
    });

  }

  showOpenVasData() {
    this.showOpenVas = !this.showOpenVas;
    this.showNmap = false;
  }

  showNmapData() {
    this.showNmap = !this.showNmap;
    this.showOpenVas = false;
  }

  selectOpenvas(event: Event, selectedOpenvas: any) {
    this.selectedOpenvas = selectedOpenvas;
    this.displayDialog = true;
    event.preventDefault();
  }

  selectNmapHost(event: Event, selectedNmpaHost: any) {
    this.selectedNmapHost = selectedNmpaHost;
    this.displayDialog = true;
    event.preventDefault();
  }

  onDialogHide() {
    this.selectedOpenvas = null;
    this.selectedNmapHost = null;
    this.displayDialog = false;
  }

}

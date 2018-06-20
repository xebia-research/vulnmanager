import {Component, OnInit} from '@angular/core';
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {
  uploadedFiles: any[] = [];
  hostName: any = location.hostname;
  companyName:any;
  teamName:any;
  protocol: any = location.protocol;

  constructor() {

  BASE_URL:any;
  DOMAIN_URL:any = location.protocol + '//' + 'vulnapi.' + location.hostname;
  LAN_URL:any = location.protocol + '//' + location.hostname + ':4343';

  constructor(private router:Router) {

    if(/^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/.test(location.hostname)) {
      this.BASE_URL = this.LAN_URL;
    } else {
      this.BASE_URL = this.DOMAIN_URL;
    }
  }

  ngOnInit() {
    this.companyName = localStorage.getItem("company");
    this.teamName = localStorage.getItem("selectedTeam");

    this.router.events.subscribe((event => {
      if(event instanceof NavigationEnd) {
        this.teamName = localStorage.getItem("selectedTeam");
      }
    }));
  }

  onBeforeSend = function (event) {
    this.teamName = localStorage.getItem("selectedTeam");
    event.xhr.setRequestHeader('authorization', localStorage.getItem("jwt"));
  };

  onUpload(event) {
    for (let file of event.files) {
      this.uploadedFiles.push(file);
      console.log(this.BASE_URL)
    }

  }

  onError(event) {
    console.log(event)
  }

}

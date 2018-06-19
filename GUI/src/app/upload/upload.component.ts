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

  constructor(private router:Router) {

  }

  ngOnInit() {
    this.companyName = localStorage.getItem("company");
    this.teamName = localStorage.getItem("selectedTeam");

    this.router.events.subscribe((event => {
      if(event instanceof NavigationEnd) {
        this.teamName = localStorage.getItem("selectedTeam");
      }
    }))
  }

  onBeforeSend = function (event) {
    this.teamName = localStorage.getItem("selectedTeam");
    event.xhr.setRequestHeader('authorization', localStorage.getItem("jwt"));
  };

  onUpload(event) {
    for (let file of event.files) {
      this.uploadedFiles.push(file);
    }

  }

  onError(event) {
    console.log(event)
  }

}

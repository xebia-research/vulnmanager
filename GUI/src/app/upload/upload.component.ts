import {Component, OnInit} from '@angular/core';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {
  uploadedFiles: any[] = [];
  hostName: any = location.hostname;

  constructor() {

  }

  ngOnInit() {
  }

  onBeforeSend = function (event) {
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

import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent implements OnInit {
  uploadedFiles: any[] = [];

  constructor() {

  }

  ngOnInit() {
  }


  onUpload(event) {
    console.log("upload succeeds");
    for(let file of event.files) {
      this.uploadedFiles.push(file);
    }

  }
  onError(event) {
    for (let hoi of event) {
      console.log(hoi);
    }
    console.log(event);
  }


}

import {Component, OnInit} from '@angular/core';
import {MessageService} from 'primeng/components/common/messageservice';
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css'],
  providers: [MessageService]
})
export class UploadComponent implements OnInit {
  uploadedFiles: any[] = [];
  hostName: any = location.hostname;

  companyName:any;
  teamName:any;
  protocol: any = location.protocol;

  BASE_URL:any;
  DOMAIN_URL:any = location.protocol + '//' + 'vulnapi.' + location.hostname;
  LAN_URL:any = location.protocol + '//' + location.hostname + ':4343';

  constructor(private router:Router, private messageService: MessageService) {
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
    }

    let httpSuccessfulMsg = JSON.parse(event.xhr.responseText);
    this.addSuccessfulUploadMessages(httpSuccessfulMsg);
  }

  onError(event) {
    let httpErrorMsg = JSON.parse(event.xhr.responseText);

    let successfullyMsgList = httpErrorMsg['successfullyUploadedMsgs'];
    let errorMsgList = httpErrorMsg['errorUploadingMsgs'];

    if (errorMsgList != null) {
      // It is still possible some files are successfully uploaded
      if (successfullyMsgList != null) {
        this.addSuccessfulUploadMessages(successfullyMsgList);
      }

      this.addErrorUploadMessages(errorMsgList);
    } else {
      this.addSingleErrorMsg(httpErrorMsg['msg']);
    }

    for (let file of event.files) {
      this.uploadedFiles.push(file);
    }
  }

  addSuccessfulUploadMessages(successfulMessages) {
    successfulMessages.forEach((message) => {
      this.addSingleSuccessfulMsg(message);
    });
  }

  addErrorUploadMessages(errorMessages) {
    errorMessages.forEach((message) => {
      this.addSingleErrorMsg(message);
    });
  }

  addSingleSuccessfulMsg(msg) {
    this.messageService.add({key: 'successfulMsg', severity: 'success', summary: 'Upload successful', detail: msg});
  }

  addSingleErrorMsg(msg) {
    this.messageService.add({key: 'errorMsg', severity: 'error', summary: 'Upload Failed', detail: msg});
  }

}

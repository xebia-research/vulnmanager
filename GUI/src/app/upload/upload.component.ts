import {Component, OnInit} from '@angular/core';
import {MessageService} from 'primeng/components/common/messageservice';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css'],
  providers: [MessageService]
})
export class UploadComponent implements OnInit {
  uploadedFiles: any[] = [];
  hostName: any = location.hostname;

  constructor(private messageService: MessageService) {

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

    let httpErrorMsg = JSON.parse(event.xhr.responseText);
    this.addSuccessfulUploadMessages(httpErrorMsg);
  }

  onError(event) {
    console.log(event);
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

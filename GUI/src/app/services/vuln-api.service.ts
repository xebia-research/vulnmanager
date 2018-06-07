import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";

@Injectable()
export class VulnApiService {

  constructor(private http: HttpClient) { }

  signup(user, password, companyName) {

    let userObj:any = {};
    userObj.username = user;
    userObj.password = password;
    userObj.companyName = companyName;


    return this.http.post('http://localhost:8080/users/sign-up', userObj);
  }

  login(user, password) {
    let userObj:any = {};
    userObj.username = user;
    userObj.password = password;


    this.http.post('http://localhost:8080/login', userObj,{ observe: 'response' }).subscribe(result => {
      console.log(result);
      console.log(result.headers.has("test"));
      console.log(result.headers.get("test"));
    })
  }

}

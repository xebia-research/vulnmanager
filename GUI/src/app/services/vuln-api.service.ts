import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from "@angular/common/http";

@Injectable()
export class VulnApiService {

  BASE_URL:any =  "http://localhost:8080";

  constructor(private http: HttpClient) { }

  signup(user, password, companyName) {

    let userObj:any = {};
    userObj.username = user;
    userObj.password = password; // reverse with atob
    userObj.companyName = companyName;


    return this.http.post(this.BASE_URL + '/users/sign-up', userObj);
  }

  login(user, password) {
    let userObj:any = {};
    userObj.username = user;
    userObj.password = password;


    return this.http.post(this.BASE_URL + '/login', userObj,{ withCredentials: true, observe: 'response' }).subscribe(result => {
      let auth = result.headers.get("authorization");

      if(auth != null) {
        localStorage.setItem("user", userObj.username);
        localStorage.setItem("pass", userObj.password);
        localStorage.setItem("jwt", auth);
        return true;
      }
    }, error => {
      return false;
    });
  }

  addTest() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/addtest", httpOption);
  }

  getOpenvas(company, team) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/openvas", httpOption);
  }

  getNmap(company, team) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/nmap", httpOption);
  }

  getZap(company, team) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/zap", httpOption);
  }

  getClair(company, team) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/clair", httpOption);
  }

  getGenericMulti(company, team) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/generic", httpOption);
  }

  getGenericReport(company, team, id) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/generic/report/" + id, httpOption);
  }

  getGenericResult(company, team, reportid, resultid) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/generic/report/" + reportid + "/result/" + resultid, httpOption);
  }

  postComment(company, team, reportid, resultid, text) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    let comment:any = {};
    comment.userName = localStorage.getItem("user");
    comment.content = text;

    return this.http.post(this.BASE_URL + "/" + company + "/" + team + "/generic/report/" + reportid + "/result/" + resultid + "/comment", comment, httpOption);
  }

  postFalsePositive(company, team, reportid, resultid) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };
    return this.http.post(this.BASE_URL + "/" + company + "/" + team + "/generic/report/" + reportid + "/result/" + resultid + "/falsePositive", {}, httpOption);
  }
}

import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";

@Injectable()
export class VulnApiService {

  BASE_URL: any = "http://localhost:8080";

  constructor(private http: HttpClient) {
  }

  signup(user, password, companyName) {

    let userObj: any = {};
    userObj.username = user;
    userObj.password = password; // reverse with atob
    userObj.companyName = companyName;


    return this.http.post(this.BASE_URL + '/users/sign-up', userObj);
  }

  login(user, password) {
    let userObj: any = {};
    userObj.username = user;
    userObj.password = password;


    return this.http.post(this.BASE_URL + '/login', userObj, {
      withCredentials: true,
      observe: 'response'
    }).subscribe(result => {
      let auth = result.headers.get("authorization");

      if (auth != null) {
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

  getOpenvasReport(company, team, reportId) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/openvas/" + reportId, httpOption);
  }

  getNmap(company, team) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/nmap", httpOption);
  }

  getNmapReport(company, team, reportId) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/nmap/" + reportId, httpOption);
  }

  getZap(company, team) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/zap", httpOption);
  }

  getReportZap(company, team, reportId) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/zap/" + reportId, httpOption);
  }

  getClair(company, team) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/clair", httpOption);
  }

  getReportClair(company, team, reportId) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/clair/" + reportId, httpOption);
  }
}

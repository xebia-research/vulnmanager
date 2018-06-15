import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { JwtHelperService, JWT_OPTIONS } from '@auth0/angular-jwt';
import { Router, CanActivate } from '@angular/router';

@Injectable()
export class VulnApiService {

  helper: JwtHelperService;
  BASE_URL: any = location.protocol + '//' + location.hostname + ':4343';
  router: Router;

  constructor(private http: HttpClient, private jwtHelper: JwtHelperService) {

    this.helper = new JwtHelperService();
  }

  signup(user, password, companyName) {

    let userObj: any = {};
    userObj.username = user;
    userObj.password = btoa(password); // reverse with atob
    userObj.companyName = companyName;


    return this.http.post(this.BASE_URL + '/users/sign-up', userObj);
  }

  login(user, password) {
    let userObj: any = {};
    userObj.username = user;
    userObj.password = btoa(password);


    return this.http.post(this.BASE_URL + '/login', userObj, {
      withCredentials: true,
      observe: 'response'
    }).subscribe(result => {
      let auth = result.headers.get("authorization");

      const decodedToken = this.helper.decodeToken(auth);
      const expirationDate = this.helper.getTokenExpirationDate(auth);
      const isExpired = this.helper.isTokenExpired(auth);

      console.log(decodedToken);
      console.log(expirationDate + " --- " + isExpired);

      if (auth != null) {
        localStorage.setItem("user", userObj.username);
        localStorage.setItem("jwt", auth);
        return true;
      }
      this.router.navigate(['home']);
    }, error => {
      return false;
    });

  }

  logout() {
    localStorage.removeItem("jwt");
  }

  isLoggedIn() {

    const token = localStorage.getItem('jwt');
    // Check whether the token is expired and return
    // true or false
    return !this.jwtHelper.isTokenExpired(token);
  }

  getUserNameFromToken() {
    const decodedToken = this.helper.decodeToken(localStorage.getItem("jwt"));
    return decodedToken.sub;
  }

  addTest() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/addtest", httpOption);
  }

  addTestCompany() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/addtest/company", httpOption);
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

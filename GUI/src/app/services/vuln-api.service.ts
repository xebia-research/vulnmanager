import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { JwtHelperService, JWT_OPTIONS } from '@auth0/angular-jwt';
import {reject} from "q";
import {computeStyle} from "@angular/animations/browser/src/util";

@Injectable()
export class VulnApiService {

  helper: JwtHelperService;
  BASE_URL: any;
  DOMAIN_URL: any = location.protocol + '//' + 'vulnapi.' + location.hostname;
  LAN_URL: any = location.protocol + '//' + location.hostname + ':4343'

  LS_USER = "user";
  LS_JWT = "jwt";
  LS_COMPANY = "company";
  LS_MYTEAMS = "myteams";
  LS_SELECTED_TEAM = "selectedTeam";
  LS_API_KEY = "apikey";
  LS_ALL_TEAMS = "allteams";

  constructor(private http: HttpClient, private jwtHelper: JwtHelperService) {

    if(/^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)/.test(location.hostname)) {
      this.BASE_URL = this.LAN_URL;
    } else {
      this.BASE_URL = this.DOMAIN_URL;
    }

    this.helper = new JwtHelperService();
  }

  signup(user, password, companyName) {

    let userObj: any = {};
    userObj.username = user;
    userObj.password = btoa(password); // reverse with atob
    userObj.companyName = companyName;


    let promise = new Promise((resolve, reject) => {
      this.http.post(this.BASE_URL + '/users/sign-up', userObj, {
        observe: 'response'
      }).subscribe(result => {
        resolve(result);
      }, error => {
        reject();
      });

    });

    return promise;
  }

  login(user, password) {
    let userObj: any = {};
    userObj.username = user;
    userObj.password = btoa(password);

    let promise = new Promise((resolve, reject) => {
      this.http.post(this.BASE_URL + '/login', userObj, {
        withCredentials: true,
        observe: 'response'
      }).subscribe(result => {

        let auth = result.headers.get("authorization");
        if (auth != null) {
          localStorage.setItem(this.LS_USER, userObj.username);
          localStorage.setItem(this.LS_JWT, auth);
          resolve("login")
        }
        reject(false);
      }, error => {
        reject(false);
      });
    });


    return promise;
  }

  logout() {
    localStorage.removeItem(this.LS_COMPANY);
    localStorage.removeItem(this.LS_ALL_TEAMS);
    localStorage.removeItem(this.LS_MYTEAMS);
    localStorage.removeItem(this.LS_JWT);
    localStorage.removeItem(this.LS_SELECTED_TEAM);
    localStorage.removeItem(this.LS_API_KEY);

  }

  isLoggedIn() {

    const token = localStorage.getItem(this.LS_JWT);
    // Check whether the token is expired and return
    // true or false
    return !this.jwtHelper.isTokenExpired(token);
  }

  getUserNameFromToken() {
    const decodedToken = this.helper.decodeToken(localStorage.getItem(this.LS_JWT));
    return decodedToken.sub;
  }

  whoami() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/users/whoami", httpOption);
  }

  whoMyCompany() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/users/whomycompany", httpOption);
  }

  whoMyTeam() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/users/whomyteams", httpOption);
  }

  getAllInfoFromServer() {
    let promise = new Promise((resolve, reject) => {
      if(localStorage.getItem(this.LS_JWT) != null) {
        this.whoami().subscribe((res) => {
          let user:any = res;
          console.log(res);
          localStorage.setItem(this.LS_API_KEY, user.apiKey);
          localStorage.setItem("user", user.username);
          this.whoMyCompany().subscribe(res2 => {

            let company: any = res2;
            if(company.msg == null) {
              localStorage.setItem(this.LS_COMPANY, company.name);
              localStorage.setItem(this.LS_ALL_TEAMS, JSON.stringify(company.teams))

              this.whoMyTeam().subscribe(res3 => {
                let myTeams: any = res3;
                localStorage.setItem(this.LS_MYTEAMS, JSON.stringify(myTeams))
                resolve("Loaded everything")
              })
            }
          })
        });
      } else {
        resolve("ERROR: no Auth token found");
      }
    });
    return promise;
  }

  setSelectedTeam(teamName) {
    localStorage.setItem(this.LS_SELECTED_TEAM, teamName);
  }

  getSelectedTeam() {
    return localStorage.getItem(this.LS_SELECTED_TEAM);
  }

  delete() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    this.logout()

    return this.http.get(this.BASE_URL + "/addtest/delete", httpOption);
  }

  isTestAdded() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/addtest/done", httpOption);
  }

  addTest() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/addtest", httpOption);
  }

  addTestCompany() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/addtest/company", httpOption);
  }

  getOpenvas() {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/openvas", httpOption);
  }

  getOpenvasReport(reportId) {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/openvas/" + reportId, httpOption);
  }

  getNmap() {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/nmap", httpOption);
  }

  getNmapReport(reportId) {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/nmap/" + reportId, httpOption);
  }

  getZap() {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/zap", httpOption);
  }

  getReportZap(reportId) {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/zap/" + reportId, httpOption);
  }

  getClair() {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/clair", httpOption);
  }

  getReportClair(reportId) {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/clair/" + reportId, httpOption);
  }

  getGenericMulti() {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/generic", httpOption);
  }

  getGenericReport(id) {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/generic/report/" + id, httpOption);
  }

  getGenericResult(reportid, resultid) {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/generic/report/" + reportid + "/result/" + resultid, httpOption);
  }

  postComment(reportid, resultid, text) {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };

    let comment:any = {};
    comment.userName = localStorage.getItem("user");
    comment.content = text;

    return this.http.post(this.BASE_URL + "/" + company + "/" + team + "/generic/report/" + reportid + "/result/" + resultid + "/comment", comment, httpOption);
  }

  postFalsePositive(reportid, resultid) {
    let company:any = localStorage.getItem(this.LS_COMPANY);
    let team:any = localStorage.getItem(this.LS_SELECTED_TEAM);

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };
    return this.http.post(this.BASE_URL + "/" + company + "/" + team + "/generic/report/" + reportid + "/result/" + resultid + "/falsePositive", {}, httpOption);
  }

  getCompany(name) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };
    return this.http.get(this.BASE_URL + "/" + name, httpOption);
  }

  postCompany(name) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };
    return this.http.post(this.BASE_URL + "/" + name, {}, httpOption);
  }

  postTeam(companyName, teamName) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem(this.LS_JWT)
      })
    };
    return this.http.post(this.BASE_URL + "/" + companyName + "/" + teamName, {}, httpOption);
  }


}

import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { JwtHelperService, JWT_OPTIONS } from '@auth0/angular-jwt';

@Injectable()
export class VulnApiService {

  helper: JwtHelperService;
  BASE_URL: any;
  DOMAIN_URL: any = location.protocol + '//' + 'vulnapi.' + location.hostname;
  LAN_URL: any = location.protocol + '//' + location.hostname + ':4343'


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

      if (auth != null) {
        localStorage.setItem("user", userObj.username);
        localStorage.setItem("jwt", auth);
        return true;
      }
    }, error => {
      return false;
    });
  }

  logout() {
    localStorage.removeItem("company");
    localStorage.removeItem("allteams");
    localStorage.removeItem("myteams");
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

  whoami() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/users/whoami", httpOption);
  }

  whoMyCompany() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/users/whomycompany", httpOption);
  }

  whoMyTeam() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/users/whomyteams", httpOption);
  }

  getAllInfoFromServer() {
    let promise = new Promise((resolve, reject) => {
      if(localStorage.getItem("jwt") != null) {
        this.whoami().subscribe((res) => {
          let user:any = res;
          localStorage.setItem("user", user.username);
          this.whoMyCompany().subscribe(res2 => {

            let company: any = res2;
            if(company.msg == null) {
              localStorage.setItem("company", company.name);
              localStorage.setItem("allteams", JSON.stringify(company.teams))

              this.whoMyTeam().subscribe(res3 => {
                let myTeams: any = res3;
                localStorage.setItem("myteams", JSON.stringify(myTeams))
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
    localStorage.setItem("selectedTeam", teamName);
  }

  getSelectedTeam(teamName) {
    return localStorage.getItem("selectedTeam");
  }

  delete() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    this.logout()

    return this.http.get(this.BASE_URL + "/addtest/delete", httpOption);
  }

  isTestAdded() {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/addtest/done", httpOption);
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

  getOpenvas() {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/openvas", httpOption);
  }

  getOpenvasReport(reportId) {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/openvas/" + reportId, httpOption);
  }

  getNmap() {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/nmap", httpOption);
  }

  getNmapReport(reportId) {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/nmap/" + reportId, httpOption);
  }

  getZap() {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/zap", httpOption);
  }

  getReportZap(reportId) {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/zap/" + reportId, httpOption);
  }

  getClair() {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/clair", httpOption);
  }

  getReportClair(reportId) {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/clair/" + reportId, httpOption);
  }

  getGenericMulti() {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/generic", httpOption);
  }

  getGenericReport(id) {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/generic/report/" + id, httpOption);
  }

  getGenericResult(reportid, resultid) {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };

    return this.http.get(this.BASE_URL + "/" + company + "/" + team + "/generic/report/" + reportid + "/result/" + resultid, httpOption);
  }

  postComment(reportid, resultid, text) {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }

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

  postFalsePositive(reportid, resultid) {
    let company:any = localStorage.getItem("company");
    let team:any = localStorage.getItem("selectedTeam");

    if(team == null || company == null) {
      return;
    }

    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };
    return this.http.post(this.BASE_URL + "/" + company + "/" + team + "/generic/report/" + reportid + "/result/" + resultid + "/falsePositive", {}, httpOption);
  }

  getCompany(name) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };
    return this.http.get(this.BASE_URL + "/" + name, httpOption);
  }

  postCompany(name) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };
    return this.http.post(this.BASE_URL + "/" + name, {}, httpOption);
  }

  postTeam(companyName, teamName) {
    const httpOption = {
      headers: new HttpHeaders({
        'authorization': localStorage.getItem("jwt")
      })
    };
    return this.http.post(this.BASE_URL + "/" + companyName + "/" + teamName, {}, httpOption);
  }


}

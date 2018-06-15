import {Component, OnInit} from '@angular/core';
import {VulnApiService} from "../services/vuln-api.service";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  visibleSidebar1: boolean;
  isLoggedIn:boolean;
  username: any;

  constructor(private apiService: VulnApiService) {
  }

  logout() {
    this.apiService.logout();
    window.location.reload();
  }

  ngOnInit() {
    this.isLoggedIn = this.apiService.isLoggedIn();

    if(this.isLoggedIn) {
      this.username = this.apiService.getUserNameFromToken();
    }
  }

  runAddTest() {
    this.apiService.addTest().subscribe(() => {
      alert("The example logs are added, Press OK to reload the page");
      window.location.reload();
    });
  }

  runAddCompany() {
    this.apiService.addTestCompany().subscribe(() => {
      alert("The Company is added, reloading page");
      window.location.reload();
    });
  }

}

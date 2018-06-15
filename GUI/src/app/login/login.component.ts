import { Component, OnInit } from '@angular/core';
import { VulnApiService } from '../services/vuln-api.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [VulnApiService]
})
export class LoginComponent implements OnInit {

  user:any;

  constructor(private apiService:VulnApiService) { }

  ngOnInit() {
    this.user = {};
  }

  Register() {
    console.log("SUBMITTING!")
    console.log(this.user);
    this.user.companyName = "";
    this.apiService.signup(this.user.username, this.user.password, this.user.companyName)
      .subscribe(result => {
        console.log(result);
        this.apiService.login(this.user.username, this.user.password);
      });
  }

  Login() {
    console.log("SUBMITTING!")
    console.log(this.user);
    this.user.companyName = "";
    this.apiService.login(this.user.username, this.user.password);
  }

}

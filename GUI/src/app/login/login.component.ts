import { Component, OnInit } from '@angular/core';
import { VulnApiService } from '../services/vuln-api.service';
import {Router} from "@angular/router";
import {Message} from 'primeng/api';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  providers: [VulnApiService]
})
export class LoginComponent implements OnInit {

  user:any;
  msgs: Message[] = [];

  constructor(private apiService:VulnApiService, private router: Router) { }

  ngOnInit() {
    this.user = {};

    if (this.apiService.isLoggedIn()) {
      this.router.navigate(['/home']);
    }

  }



  Login() {
    this.apiService.logout();
    console.log("SUBMITTING!")
    console.log(this.user);
    this.user.companyName = "";
    this.apiService.login(this.user.username, this.user.password).then((res) => {
      this.router.navigate(['/home']);
    }).catch(error => {
      this.msgs.push({severity:'error', summary:'Error Message', detail:'Details not correct'});
    })
  }

}

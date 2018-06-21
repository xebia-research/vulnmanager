import { Component, OnInit } from '@angular/core';
import { VulnApiService } from '../services/vuln-api.service';
import {Router} from "@angular/router";
import {Message} from 'primeng/api';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  user:any;
  msgs: Message[] = [];

  constructor(private apiService:VulnApiService, private router: Router) { }

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
        this.apiService.login(this.user.username, this.user.password).then(res => {
          this.router.navigate(['/company']);
        }).catch(error => {
          this.msgs.push({severity:'error', summary:'Error Message', detail:'Error logging in'});
        })
      });
  }
}

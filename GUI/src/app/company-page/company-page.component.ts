import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MenuItem, Message} from "primeng/api";
import {VulnApiService} from "../services/vuln-api.service";
import {Router} from "@angular/router";
import {MessageService} from 'primeng/components/common/messageservice';

@Component({
  providers:[MessageService],
  selector: 'app-company-page',
  templateUrl: './company-page.component.html',
  styleUrls: ['./company-page.component.css'],
  encapsulation: ViewEncapsulation.None
})
export class companyComponent implements OnInit {
  constructor(private apiService: VulnApiService, private router: Router,private msgService: MessageService) { }

  msgs: Message[] = [];

  companyFound:boolean;
  companyName:any;
  teamName:any;
  teams:any;
  includedTeams: any;


  ngOnInit() {
    this.companyFound = false;

    this.apiService.whoami().subscribe(res1 => {
      console.log(res1);
      this.apiService.whoMyCompany().subscribe(res2 => {
        console.log(res2);
        let company:any = res2;
        console.log(company)
        this.companyFound = true;
        this.companyName = company.name;
        this.teams = company.teams;
        this.apiService.whoMyTeam().subscribe(res3 => {
          let teams:any = res3;
          console.log(teams);
          this.includedTeams = teams;
        })
      })
    })
  }

  newCompanySubmit(f) {
    console.log(f.value.companyName);
    this.apiService.postCompany(f.value.companyName).subscribe((res) => {
      console.log(res);
      let company:any = res;
      if(company.msg != null) {
        this.msgs.push({severity:"succes", summary: "No company found"})
      } else {
        this.companyFound = true;
        this.companyName = company.name;
        this.teams = company.teams;
      }
    });
  }

  newTeamSubmit(g) {
    console.log(g.value.teamName);
    this.apiService.postTeam(this.companyName, g.value.teamName).subscribe((res) => {
      console.log(res);
    })
  }
}



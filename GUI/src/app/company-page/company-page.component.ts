import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MenuItem, Message} from "primeng/api";
import {VulnApiService} from "../services/vuln-api.service";
import {NavigationEnd, Router} from "@angular/router";
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
  company:any;
  companyFound:boolean;
  companyName:any;
  teamName:any;
  teams:any;
  includedTeams: any;
  otherTeams:any[] = [];

  ngOnInit() {
    this.router.onSameUrlNavigation = "reload";
    this.loadData();
    this.router.events.subscribe(
      value => {
        if (value instanceof NavigationEnd) {

          this.loadData();
        }
      });

  }

  loadData() {
    this.companyFound = false;

    this.apiService.getAllInfoFromServer().then((result) => {
      this.companyName = localStorage.getItem("company");
      console.log(this.companyName);

      if(this.companyName != null && this.companyName != 'undefined') {
        this.apiService.whoMyCompany().subscribe(
          res => { this.company = res; console.log(res);}
        );
        this.companyFound = true;
        this.teams = JSON.parse(localStorage.getItem("allteams"));
        this.includedTeams = JSON.parse(localStorage.getItem("myteams"));

        this.otherTeams = [];

        for(let i = 0; i < this.teams.length; i++) {
          let found:boolean = false;
          for(let j = 0; j < this.includedTeams.length; j++) {
            if(this.includedTeams[j].name == this.teams[i].name) {
              found = true;
              break;
            }
          }
          if(!found) {
            this.otherTeams.push(this.teams[i]);
          }
        }

      } else {
        this.companyName = null;
      }
    }).catch((reason => {
      this.msgs.push({severity: "error", summary:"" + reason});
    }));
  }

  joinTeam(teamName) {
    this.apiService.postTeam(this.companyName, teamName).subscribe((res) => {
      this.addMessage("Joined team");
    });
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
        this.addMessage("Joined company");
      }
    });
  }

  newTeamSubmit(g) {
    console.log(g.value.teamName);
    this.apiService.postTeam(this.companyName, g.value.teamName).subscribe((res) => {
      this.addMessage("Team added and joined");
    })
  }

  addMessage(message) {
    this.router.navigateByUrl("/company");
    this.msgs = [];
    this.msgs.push({severity: "succes", summary: message});
  }
}



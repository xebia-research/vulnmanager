import {Component, OnInit} from '@angular/core';
import {VulnApiService} from "../services/vuln-api.service";
import {MessageService} from "primeng/components/common/messageservice";
import {MenuItem, Message} from "primeng/api";
import {NavigationEnd, Router} from "@angular/router";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  providers: [MessageService],
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  isLoggedIn:boolean;
  username: any;

  items: MenuItem[];
  msgs: Message[] = [];

  selectedTeam:any;
  teams:any[];

  constructor(private apiService: VulnApiService, private msgService: MessageService, private router:Router) {

  }

  logout() {
    this.apiService.logout();
    window.location.reload();
  }

  login() {
    this.router.navigateByUrl("/login");
  }

  onTeamSelectChange(event) {
    if(event != null) {
      this.apiService.setSelectedTeam(event.name);
      this.router.navigateByUrl(this.router.url);
    } else {
      this.apiService.setSelectedTeam("");
    }
  }

  ngOnInit() {

    this.router.onSameUrlNavigation = "reload";
    this.router.events.subscribe(
      value => {

        if(value instanceof NavigationEnd){


          this.isLoggedIn = this.apiService.isLoggedIn();

          this.apiService.getAllInfoFromServer().then((res) => {
            let result:any = res;
            if(!result.startsWith("ERROR")) {
              this.teams = JSON.parse(localStorage.getItem("myteams"));
              console.log(this.teams);
              console.log(this.apiService.getSelectedTeam());

              if (this.apiService.getSelectedTeam() == null) {
                this.apiService.setSelectedTeam(this.teams[0].name);
                console.log(this.apiService.getSelectedTeam());
              }
            }
          })

          if(this.isLoggedIn) {
            this.username = this.apiService.getUserNameFromToken();

            this.apiService.whoami().subscribe((res) => {

            })

          }
        }
    }
    )

  runAddTest() {
    this.apiService.addTest().subscribe(() => {
      alert("The example logs are added, Press OK to reload the page");
      window.location.reload();
    });
  }

  runDelete() {
    if(confirm("DELETE EVERYTHING WITH THANKS TO YOU")) {
      this.apiService.delete().subscribe(() => {
        this.msgs.push({severity:'success', summary:'Everything was lost and world is now burning thanks to you'});
        this.router.navigateByUrl("/login");
        //window.location.reload();
      });
    }
  }

}

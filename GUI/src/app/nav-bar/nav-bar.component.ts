import {Component, OnInit} from '@angular/core';
import {VulnApiService} from "../services/vuln-api.service";
import {MessageService} from "primeng/components/common/messageservice";
import {MenuItem, Message} from "primeng/api";
import {Router} from "@angular/router";

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

  constructor(private apiService: VulnApiService, private msgService: MessageService, private router:Router) {
  }

  logout() {
    this.apiService.logout();
    window.location.reload();
  }

  login() {
    this.router.navigateByUrl("/login");
  }

  ngOnInit() {
    this.isLoggedIn = this.apiService.isLoggedIn();

    if(this.isLoggedIn) {
      this.username = this.apiService.getUserNameFromToken();

      this.apiService.whoami().subscribe((res) => {
        console.log(res);
      })

    }


    this.items = [
      {
        label: 'Home',
        routerLink: '/',
        icon: 'fa fa-home'
      },
      {
        label: 'Reports',
        icon: 'fa fa-file',
        items: [
          {label: 'Nmap', icon: 'fa fa-eye', routerLink: '/nmap-select-report'},
          {label: 'Openvas', icon: 'fab fa-d-and-d', routerLink: '/openvas-select-report'},
          {label: 'ZAP', icon: 'fa fa-bolt', routerLink: '/zap-select-report'},
          {label: 'Clair', icon: 'fab fa-docker', routerLink: '/clair-select-report'},
        ]
      },
      {
        label: 'Generic',
        routerLink: '/generic-results',
        icon: 'fa fa-book'
      },
      {
        label: 'Upload',
        routerLink: '/upload',
        icon: 'fas fa-cloud-upload-alt'
      },
      {
        label: 'Unsafe',
        icon: 'fas fa-skull',
        items: [
          {
            label: 'DELETE EVERYTHING', icon: 'fas fa-skull',
            command: (event: any) => {
              this.runDelete();
            }
          }
        ]
      }
    ];
  }

  runDelete() {
    let names:any = [];
    names[0] = "THE Dofensmithzinator";
    names[1] = "DOLBY";
    names[2] = "YOUUUU";
    names[3] = "SNOOP DAWG";
    names[4] = "A WRONG CLICK ON 420";

    if(confirm("DELETE EVERYTHING WITH THANKS TO "+ names[Math.floor(Math.random() * Math.floor(names.length))])) {
      this.apiService.delete().subscribe(() => {
        this.msgs.push({severity:'success', summary:'Everything was lost and world is now burning thanks to you'});
        this.router.navigateByUrl("/login");
        //window.location.reload();
      });
    }
  }

}

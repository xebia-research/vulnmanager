import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {MenuItem, Message} from "primeng/api";
import {VulnApiService} from "../services/vuln-api.service";
import {Router} from "@angular/router";
import {MessageService} from 'primeng/components/common/messageservice';

@Component({
  providers:[MessageService],
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
  styles: [`
        .ui-steps .ui-steps-item {
            width: 33.333%;
        }
  `],
  encapsulation: ViewEncapsulation.None
})
export class HomePageComponent implements OnInit {
  constructor(private apiService: VulnApiService, private router: Router,private msgService: MessageService) { }

  items: MenuItem[];
  activeIndex: number = 0;
  msgs: Message[] = [];


  ngOnInit() {

    this.items = [{
      label: 'Log in',
      command: (event: any) => {
        this.activeIndex = 1;
        this.items[0].label = "Log in done";
        this.router.navigateByUrl('/login');
      }
    },
      {
        label: 'Company',
        command: (event: any) => {
          this.apiService.addTestCompany().subscribe(() => {
            this.activeIndex = 2;
            this.items[1].disabled = true;
            this.items[1].label = "Company done";
            this.msgs.splice(0, 1);
            this.msgs.push({severity:'success', summary:'Test company was added'})
          });
        }
      },
      {
        label: 'Reports',
        command: (event: any) => {
          this.apiService.addTest().subscribe(() => {
            this.activeIndex = 3;
            this.items[2].disabled = true;
            this.items[2].label = "Reports done"
            this.msgs.splice(0, 1);
            this.msgs.push({severity:'success', summary:'Test reports were added'})
          });
        }
      }
    ];

    this.apiService.isTestAdded().subscribe((res) => {
      let result:any = res;

      if(result.accounts) {
        this.activeIndex = 1;
        this.items[0].disabled = true;
        this.items[0].label = "Log in done";;
        if(result.company) {
          this.activeIndex = 2;
          this.items[1].disabled = true;
          this.items[1].label = "Company done";
          if(result.reports) {
            this.activeIndex = 3;
            this.items[2].disabled = true;
            this.items[2].label = "Reports done";
          }
        }
      }

    })
  }
}



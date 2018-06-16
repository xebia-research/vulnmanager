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
            width: 25%;
        }
    `],
  encapsulation: ViewEncapsulation.None
})
export class HomePageComponent implements OnInit {
  constructor(private apiService: VulnApiService, private router: Router,private msgService: MessageService) { }

  items: MenuItem[];
  activeIndex: number = 1;
  msgs: Message[] = [];


  ngOnInit() {

    this.items = [{
      label: 'Log in',
      command: (event: any) => {
        this.activeIndex = 1;
        this.router.navigateByUrl('/login');
      }
    },
      {
        label: 'Company',
        command: (event: any) => {
          this.activeIndex = 2;
          this.apiService.addTestCompany().subscribe(() => {
            this.msgs.push({severity:'success', summary:'Test company was added'})

          });
        }
      },
      {
        label: 'Reports',
        command: (event: any) => {
          this.activeIndex = 3;
          this.apiService.addTest().subscribe(() => {
            this.msgs.push({severity:'success', summary:'Test reports were added'})
          });
        }
      },
      {
        label: 'Refresh',
        command: (event: any) => {
          this.activeIndex = 0;
          window.location.reload();

        }
      }
    ];

  }
  }



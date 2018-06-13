import {Component, OnInit} from '@angular/core';
import {VulnApiService} from "../services/vuln-api.service";

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  visibleSidebar1: boolean;

  constructor(private apiService: VulnApiService) {
  }

  ngOnInit() {
  }

  runAddTest() {
    this.apiService.addTest().subscribe(() => {
    });
    window.location.reload();

    alert("addTest ran successfully");
  }

}

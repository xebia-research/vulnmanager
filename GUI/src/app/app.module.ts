import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";

import {ButtonModule} from 'primeng/button';
import {SplitButtonModule} from 'primeng/splitbutton';
import {TableModule} from 'primeng/table';
import {DataViewModule} from 'primeng/dataview';
import {DialogModule} from 'primeng/dialog';
import {HttpClientModule} from '@angular/common/http';
import {PaginatorModule} from 'primeng/paginator';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SidebarModule} from 'primeng/sidebar';
import {FieldsetModule} from 'primeng/fieldset';
import { AccordionModule} from "primeng/primeng";
import {TabViewModule} from "primeng/primeng";
import { AppComponent } from './app.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { NmapResultsComponent } from './nmap-results/nmap-results.component';
import { OpenvasResultsComponent } from './openvas-results/openvas-results.component';
import { HomePageComponent } from './home-page/home-page.component';
import { LoginComponent } from './login/login.component';
import { VulnApiService } from "./services/vuln-api.service";
import {ClairResultsComponent} from './clair-results/clair-results.component';
import {ZapResultsComponent} from "./zap-results/zap-results.component";
import {UploadComponent} from './upload/upload.component';
import {FileUploadModule} from 'primeng/fileupload';
import { OpenvasSelectReportComponent } from './openvas-select-report/openvas-select-report.component';
import { NmapSelectReportComponent } from './nmap-select-report/nmap-select-report.component';
import { ZapSelectReportComponent } from './zap-select-report/zap-select-report.component';
import { ClairSelectReportComponent } from './clair-select-report/clair-select-report.component';


const appRoutes: Routes = [

  {path: 'nmap-result/:id', component: NmapResultsComponent},
  {path: 'nmap-select-report', component: NmapSelectReportComponent},
  {path: 'openvas-result/:id', component: OpenvasResultsComponent},
  {path: 'openvas-select-report', component: OpenvasSelectReportComponent},
  {path: 'clair-result/:id', component: ClairResultsComponent},
  {path: 'clair-select-report', component: ClairSelectReportComponent},
  {path: 'zap-result/:id' , component: ZapResultsComponent},
  {path: 'zap-select-report' , component: ZapSelectReportComponent},
  {path: 'home', component: HomePageComponent},
  {path: 'upload', component: UploadComponent},
  {path: 'login', component: LoginComponent },
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },

];

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    NmapResultsComponent,
    OpenvasResultsComponent,
    HomePageComponent,
    LoginComponent,
    ZapResultsComponent,
    ClairResultsComponent,
    UploadComponent,
    OpenvasSelectReportComponent,
    NmapSelectReportComponent,
    ZapSelectReportComponent,
    ClairSelectReportComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ButtonModule,
    SplitButtonModule,
    TableModule,
    DialogModule,
    DataViewModule,
    TabViewModule,
    PaginatorModule,
    FieldsetModule,
    AccordionModule,
    BrowserAnimationsModule,
    SidebarModule,
    FileUploadModule,
    RouterModule.forRoot(
      appRoutes,
      {enableTracing: false} // <-- debugging purposes only
    )
    // other imports here
  ],
  providers: [VulnApiService],
  bootstrap: [AppComponent]
})
export class AppModule {
}




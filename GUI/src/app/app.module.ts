import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {RouterModule, Routes} from "@angular/router";

import {ButtonModule} from 'primeng/button';
import {SplitButtonModule} from 'primeng/splitbutton';
import {TableModule} from 'primeng/table';
import {DataViewModule} from 'primeng/dataview';
import {DialogModule} from 'primeng/dialog';
import {CardModule} from "primeng/card";
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
import { GenericResultsComponent } from './generic-results/generic-results.component';
import {CheckboxModule} from 'primeng/checkbox';
import { OpenvasSelectReportComponent } from './openvas-select-report/openvas-select-report.component';
import { NmapSelectReportComponent } from './nmap-select-report/nmap-select-report.component';
import { ZapSelectReportComponent } from './zap-select-report/zap-select-report.component';
import { ClairSelectReportComponent } from './clair-select-report/clair-select-report.component';
import {ScrollPanelModule} from 'primeng/scrollpanel';
import {FormsModule} from "@angular/forms";
import {StepsModule} from 'primeng/steps';
import {GrowlModule} from 'primeng/growl';
import { AuthGuardService as AuthGuard} from "./services/auth-guard.service";
import { JwtHelperService, JwtModule, JWT_OPTIONS } from '@auth0/angular-jwt';
import { RegisterComponent } from './register/register.component';
import {MenubarModule} from "primeng/menubar";
import {PanelMenuModule} from "primeng/primeng";
import {companyComponent} from "./company-page/company-page.component"

const appRoutes: Routes = [

  {path: 'nmap-result/:id', component: NmapResultsComponent, canActivate: [AuthGuard] },
  {path: 'nmap-select-report', component: NmapSelectReportComponent, canActivate: [AuthGuard] },
  {path: 'openvas-result/:id', component: OpenvasResultsComponent, canActivate: [AuthGuard]},
  {path: 'openvas-select-report', component: OpenvasSelectReportComponent, canActivate: [AuthGuard]},
  {path: 'clair-result/:id', component: ClairResultsComponent, canActivate: [AuthGuard]},
  {path: 'clair-select-report', component: ClairSelectReportComponent, canActivate: [AuthGuard]},
  {path: 'zap-result/:id' , component: ZapResultsComponent, canActivate: [AuthGuard]},
  {path: 'zap-select-report' , component: ZapSelectReportComponent, canActivate: [AuthGuard]},
  {path: 'generic-results' , component: GenericResultsComponent, canActivate: [AuthGuard]},
  {path: 'company' , component: companyComponent, canActivate: [AuthGuard]},
  {path: 'home', component: HomePageComponent, canActivate: [AuthGuard]},
  {path: 'upload', component: UploadComponent, canActivate: [AuthGuard]},
  {path: 'login', component: LoginComponent },
  {path: 'register', component: RegisterComponent },
  {
    path: '',
    redirectTo: '/home',
    pathMatch: 'full'
  },

];

export function tokenGetter() {
  return localStorage.getItem('jwt');
}

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
    GenericResultsComponent,
    OpenvasSelectReportComponent,
    NmapSelectReportComponent,
    ZapSelectReportComponent,
    ClairSelectReportComponent,
    companyComponent,
    RegisterComponent
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
    GrowlModule,
    FieldsetModule,
    AccordionModule,
    BrowserAnimationsModule,
    FormsModule,
    SidebarModule,
    ScrollPanelModule,
    FileUploadModule,
    CardModule,
    CheckboxModule,
    StepsModule,
    JwtModule,
    MenubarModule,
    PanelMenuModule,
    RouterModule.forRoot(
      appRoutes,
      {enableTracing: false} // <-- debugging purposes only
    ),
    JwtModule.forRoot({
      config: {
        tokenGetter: tokenGetter,
        whitelistedDomains: ['localhost:4343'],
        blacklistedRoutes: ['localhost:4343/login/']
      }
    })
    // other imports here
  ],
  providers: [VulnApiService, JwtHelperService, AuthGuard],
  bootstrap: [AppComponent]
})
export class AppModule {
}




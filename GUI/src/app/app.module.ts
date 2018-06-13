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


const appRoutes: Routes = [

  {path: 'nmap-results', component: NmapResultsComponent},
  {path: 'openvas-results', component: OpenvasResultsComponent},
  {path: 'clair-results', component: ClairResultsComponent},
  {path: 'zap-results' , component: ZapResultsComponent},
  {path: 'generic-results' , component: GenericResultsComponent},
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
    GenericResultsComponent
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
    CardModule,
    CheckboxModule,
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




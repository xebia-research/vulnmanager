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
import {AccordionModule, TabViewModule} from "primeng/primeng";
import {AppComponent} from './app.component';
import {NavBarComponent} from './nav-bar/nav-bar.component';
import {NmapResultsComponent} from './nmap-results/nmap-results.component';
import {OpenvasResultsComponent} from './openvas-results/openvas-results.component';
import {HomePageComponent} from './home-page/home-page.component';
import {ClairResultsComponent} from './clair-results/clair-results.component';
import {UploadComponent} from './upload/upload.component';
import {FileUploadModule} from 'primeng/fileupload';

const appRoutes: Routes = [
  {path: 'nmap-results', component: NmapResultsComponent},
  {path: 'openvas-results', component: OpenvasResultsComponent},
  {path: 'clair-results', component: ClairResultsComponent},
  {path: 'home', component: HomePageComponent},
  {path: 'upload', component: UploadComponent},
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
    ClairResultsComponent,
    UploadComponent
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
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}




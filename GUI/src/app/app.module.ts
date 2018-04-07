import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { RouterModule, Routes} from "@angular/router";

import {ButtonModule} from 'primeng/button';
import {TableModule} from 'primeng/table';
import {DataViewModule} from 'primeng/dataview';
import {DialogModule} from 'primeng/dialog';
import {HttpClientModule } from '@angular/common/http';
import {PaginatorModule} from 'primeng/paginator';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {SidebarModule} from 'primeng/sidebar';

import { AppComponent } from './app.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { NmapResultsComponent } from './nmap-results/nmap-results.component';
import { OpenvasResultsComponent } from './openvas-results/openvas-results.component';
import { HomePageComponent } from './home-page/home-page.component';

const appRoutes: Routes = [
  { path: 'nmap-results', component: NmapResultsComponent },
  { path: 'openvas-results',      component: OpenvasResultsComponent },
  { path: 'home', component: HomePageComponent},
  { path: '',
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
    HomePageComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ButtonModule,
    TableModule,
    DialogModule,
    DataViewModule,
    PaginatorModule,
    BrowserAnimationsModule,
    SidebarModule,
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true } // <-- debugging purposes only
    )
    // other imports here
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }




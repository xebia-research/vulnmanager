import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import {ButtonModule} from 'primeng/button';
import {TableModule} from 'primeng/table';
import {DataViewModule} from 'primeng/dataview';
import {DialogModule} from 'primeng/dialog';
import {HttpClientModule } from '@angular/common/http';
import {PaginatorModule} from 'primeng/paginator';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { ResultsComponent } from './results/results.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';

@NgModule({
  declarations: [
    AppComponent,
    ResultsComponent,
    NavBarComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ButtonModule,
    TableModule,
    DialogModule,
    DataViewModule,
    PaginatorModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

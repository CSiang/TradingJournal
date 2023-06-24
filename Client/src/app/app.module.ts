import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { SearchStockComponent } from './components/search-stock/search-stock.component';
import { StockDetailsComponent } from './components/stock-details/stock-details.component';
import { HomeComponent } from './components/home/home.component';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations'
import { MaterialModule } from './material.module';
import { HttpService } from './Services/HttpServices';
import { RecordsComponent } from './components/records/records.component';
import { AuthService } from './Services/AuthService';
import { LoginComponent } from './components/login/login.component';
import { httpInterceptorProviders } from './_helper/http.interceptor';
import { RegisterComponent } from './components/register/register.component';
import { ProfileComponent } from './components/profile/profile.component';
import { LocalStorageService } from './_helper/LocalStorage.service';
import { ForgetPasswordComponent } from './components/forget-password/forget-password.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { AddEventComponent } from './components/add-event/add-event.component';
import { ListEventComponent } from './components/list-event/list-event.component';
import { EventService } from './Services/EventService';

@NgModule({
  declarations: [
    AppComponent,
    SearchStockComponent,
    StockDetailsComponent,
    HomeComponent,
    RecordsComponent,
    LoginComponent,
    RegisterComponent,
    ProfileComponent,
    ForgetPasswordComponent,
    ResetPasswordComponent,
    ListEventComponent,
    AddEventComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MaterialModule
  ],
  providers: [HttpService, AuthService, httpInterceptorProviders, LocalStorageService, EventService],
  bootstrap: [AppComponent]
})
export class AppModule { }

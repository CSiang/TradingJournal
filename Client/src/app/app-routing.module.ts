import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { SearchStockComponent } from './components/search-stock/search-stock.component';
import { StockDetailsComponent } from './components/stock-details/stock-details.component';
import { HomeComponent } from './components/home/home.component';
import { RecordsComponent } from './components/records/records.component';
import { LoginComponent } from './components/login/login.component';
import { RegisterComponent } from './components/register/register.component';
import { ProfileComponent } from './components/profile/profile.component';
import { ForgetPasswordComponent } from './components/forget-password/forget-password.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { AddEventComponent } from './components/add-event/add-event.component';

const routes: Routes = [
  {path:"", component: HomeComponent},
  {path:"search", component:SearchStockComponent},
  {path:"details/:symbol", component:StockDetailsComponent},
  {path:"records", component:RecordsComponent},
  {path:"login", component: LoginComponent},
  {path:"register", component: RegisterComponent},
  {path:"profile", component: ProfileComponent},
  {path:"forgetPassword", component: ForgetPasswordComponent},
  {path:"resetPassword/:resetCode", component: ResetPasswordComponent},
  {path:"addEvent", component: AddEventComponent},
  {path: "**", redirectTo:"/",pathMatch:"full" }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

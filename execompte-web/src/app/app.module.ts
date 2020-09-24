import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { FormsModule } from '@angular/forms';


import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { AppRoutingModule } from './app-routing.module';
import { MenuComponent } from './menu/menu.component';
import { FooterComponent } from './footer/footer.component';
import { LoginComponent } from './login/login.component';
import { ErrorComponent } from './error/error.component';
import { LogoutComponent } from './logout/logout.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ActivityReportComponent } from './activity-report/activity-report.component';
import { ListActivityReportComponent } from './list-activity-report/list-activity-report.component';
import { PeriodDayFilterPipe } from './pipe/period-day-filter.pipe';
import { BillingComponent } from './billing/billing.component';
import { SignupComponent } from './signup/signup.component';
import { CompanyComponent } from './company/company.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    MenuComponent,
    FooterComponent,
    LoginComponent,
    ErrorComponent,
    LogoutComponent,
    ActivityReportComponent,
    ListActivityReportComponent,
    PeriodDayFilterPipe,
    BillingComponent,
    SignupComponent,
    CompanyComponent
  ],
  imports: [
    BrowserModule,
    // import HttpClientModule after BrowserModule.
    HttpClientModule,
    AppRoutingModule,
    FormsModule,
    BrowserAnimationsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

import { NgModule } from '@angular/core';
import { RouterModule, Routes  } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { ErrorComponent } from './error/error.component';
import { LogoutComponent } from './logout/logout.component';
import { AuthGuard } from './auth/auth.guard';
import { ListActivityReportComponent } from './list-activity-report/list-activity-report.component';
import { ActivityReportComponent } from './activity-report/activity-report.component';
import { BillingComponent } from './billing/billing.component';
import { SignupComponent } from './signup/signup.component';
import { CompanyComponent } from './company/company.component';

const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'home', component: HomeComponent },
  { path: 'signup', component: SignupComponent },
  { path: 'company', component: CompanyComponent },
  { path: 'login', component: LoginComponent },
  { path: 'logout', component: LogoutComponent,  canActivate: [AuthGuard] },
  { path: 'list-activity-report', component: ListActivityReportComponent,  canActivate: [AuthGuard] },
  { path: 'list-activity-report/:id', component: ActivityReportComponent,  canActivate: [AuthGuard] },
  { path: 'list-activity-report/:id/billing', component: BillingComponent,  canActivate: [AuthGuard] },


  { path: '*', component: ErrorComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

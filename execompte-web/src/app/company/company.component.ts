import { Component, OnInit } from '@angular/core';
import { Company, Address, LegalStatus, UserDataService} from '../service/data/user-data.service';
import { AuthenticateService } from '../service/authenticate.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
  styleUrls: ['./company.component.css']
})
export class CompanyComponent implements OnInit {

  company: Company;
  username;
  //atrributes select list
  keys = Object.keys;
  legalStatuses = LegalStatus; 

  constructor(
    private router: Router,
    private authenticateService: AuthenticateService,
    private userDataService: UserDataService
    ) { }

  ngOnInit(): void {
    this.username = this.authenticateService.getUserName();
    this.company = new Company("", new Address("", "", ""), "", "", LegalStatus.EURL, "", "", "");
  }

  /**
   * createCompany
   */
  public saveCompany() {
    if (this.company.name) {
      this.userDataService.updateUserCompany(this.username, this.company)
        .subscribe(
          data => {
            console.log(data);
            this.router.navigate(['company', data]);
          }
        );

    } else {
      console.log('Invalid data !');
    }
  }

}

import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserDataService, SignUpInfo } from '../service/data/user-data.service';


@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  signUpInfo: SignUpInfo;
  invalidSignup = false;
  errorMessage= 'Please enter same password';

  constructor(
    private router: Router,
    private userDataService: UserDataService
    ) { }

  ngOnInit(): void {
    this.signUpInfo = new SignUpInfo("slashexec@gmail.com", "", "");
  }

  /**
   * authenticate
   */
  public signup() {
    this.invalidSignup = (this.signUpInfo.password) && (this.signUpInfo.password == this.signUpInfo.passwordConfirmation);
    if (this.invalidSignup) {
      console.log('Valid sign up');
      this.userDataService.signup(this.signUpInfo)
        .subscribe(
          response => {
            console.log(response.body['id']);
            console.log(response.headers.get('location'));
            //this.router.navigate(['company', response.body.id ]);
          },
          error =>{
          this.errorMessage= 'An error occured, please retry later';
          }
        );

    } else {
      console.log('Invalid sign up !');
    }

  }

}

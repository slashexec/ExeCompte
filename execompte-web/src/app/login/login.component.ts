import { Component, OnInit } from '@angular/core';
import { AuthenticateService } from '../service/authenticate.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  username = 'slashexec@gmail.com';
  password = '';
  invalidLogin = false;
  errorMessage= 'Invalid credentials';

  constructor(private authenticateService: AuthenticateService, private router: Router ) { }

  ngOnInit(): void {
    if (this.authenticateService.isUserLoggedIn()) {
      this.router.navigate(['home']);
    }
  }

  /**
   * authenticate
   */
  public authenticate() {

    if (this.authenticateService.authenticate(this.username, this.password)) {
      this.invalidLogin = false;
      console.log('Success');
      this.router.navigate(['home']);

    } else {
      this.invalidLogin = true;
      console.log('FAILED');
    }

  }

}

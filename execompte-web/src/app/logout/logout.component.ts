import { Component, OnInit } from '@angular/core';
import { AuthenticateService } from '../service/authenticate.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  constructor(private authenticateService: AuthenticateService, private router: Router) { }

  ngOnInit(): void {
    this.logout();
  }

  /**
   * logout
   */
  public logout() {
    this.authenticateService.logout();
    this.router.navigate(['/login']);

  }

}

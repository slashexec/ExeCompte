import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticateService } from '../service/authenticate.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  welcomeMessageFromService:string = '';

  constructor(private activatedRoute: ActivatedRoute,
    private router: Router,
    private authenticateService: AuthenticateService) { }

  ngOnInit(): void {
   
  }

  /**
   * signup
   */
  public signup() {
    console.log("Create account");
    this.router.navigate(['signup']);
  }

}

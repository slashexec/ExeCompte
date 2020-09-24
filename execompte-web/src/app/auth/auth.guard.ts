import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { AuthenticateService } from '../service/authenticate.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private authenticateService: AuthenticateService,
    private router: Router) {

  }
  canActivate(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot){
    
      console.log('AuthGuard called');
      if (this.authenticateService.isUserLoggedIn()) {
        return true;
      }
    
      this.router.navigate(['login']);
      return false;
  }
  
}

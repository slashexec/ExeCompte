import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AuthenticateService {

  constructor() { }

  /**
   * authenticate
   */
  public authenticate(username, password) {

    if (username==='slashexec@gmail.com' && password==='dummy') {
      sessionStorage.setItem('authenticatedUser', username);
      return true;
    }
    return false;
  }
  /**
   * logout
   */
  public logout() {
    sessionStorage.removeItem('authenticatedUser');
  }

  /**
   * isUserLoggedIn
   */
  public isUserLoggedIn() {
    return !(sessionStorage.getItem('authenticatedUser') === null);
  }
  
  /**
   * getUserName
   */
  public getUserName() {
    return sessionStorage.getItem('authenticatedUser');
  }

}

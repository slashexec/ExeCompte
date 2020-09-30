import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

export class SignUpInfo {
  constructor(
    public userName: string,
    public password: string,
    public passwordConfirmation: string
  ) {  }
}

export class Address {
  constructor(
    public street: string,
    public zipCode: string,
    public city: string
  ) {  }
}

export enum LegalStatus {
  EURL = "EURL",
  SASU = "SASU",
  MICRO_ENTREPRISE = "Micro entreprise"
}

export class Company {
  constructor(
    public name: string,
    public address: Address,
    public tel: string,
    public email: string,
    public legalStatus: LegalStatus,
    public siren: string,
    public siret: string,
    public vatNumber: string
  ) {  }
}

export class BankInfo {
  constructor(
    public iban: string,
    public bic: string
  ) {  }
}

export class User {
  constructor(
    public id: string,
    public userName: string,
    public password: string,
    public createdDate: Date,
    public company: Company,
    public bankInfo: BankInfo,
    public customers: Company[]

  ) {  }
}
@Injectable({
  providedIn: 'root'
})
export class UserDataService {

  apiUrl = environment.apiUrl;

  constructor(private http:HttpClient) { }

  signup(signUpInfo: SignUpInfo) {
    return this.http.post(`${this.apiUrl}/api/users`, signUpInfo,
    { observe: 'response', responseType: 'json'});
  }

  updateUserCompany(username, company: Company) {
    return this.http.put(`${this.apiUrl}/api/users/${username}`, company);
  }
}

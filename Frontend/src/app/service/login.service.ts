import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private baseUrl = 'http://localhost:8080/login';

  constructor(private http: HttpClient) { }

  getUser(email: string, password: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/`, {params: {email, password}});
  }
}
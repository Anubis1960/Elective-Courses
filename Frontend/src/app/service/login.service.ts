import { Injectable } from '@angular/core';
import { HttpClient, HttpRequest, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private baseUrl = 'http://localhost:8080/login';

  constructor(private http: HttpClient) { }

  getUser(email: string, password: string): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/`, {params: {email, password}});
  }
  
}

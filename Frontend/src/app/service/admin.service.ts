
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private baseUrl = 'http://localhost:8080/admin';

  constructor(private http: HttpClient) { }

  getUser(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createUser(name: string, email: string, password: string): Observable<any> {
    return this.http.post(this.baseUrl, { name, email, password });
  }

  updateUser(id: number, name: string, email: string, password: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, { name, email, password });
  }

  deleteUser(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  getUsersList(): Observable<any> {
    return this.http.get(this.baseUrl);
  }
}

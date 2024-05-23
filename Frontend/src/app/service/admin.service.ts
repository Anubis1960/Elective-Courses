
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  private baseUrl = 'http://localhost:8080/admin';

  constructor(private http: HttpClient) { }

  getAdmin(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/${id}`);
  }

  createAdmin(name: string, email: string, password: string): Observable<User> {
    const params = new HttpParams()
    .set('name', name)
    .set('email', email)
    .set('password', password);
    return this.http.post<User>(`${this.baseUrl}/`, params);
  }

  updateAdmin(id: number, name: string, email: string, password: string): Observable<User> {
    const params = new HttpParams()
    .set('name', name)
    .set('email', email)
    .set('password', password);
    return this.http.put<User>(`${this.baseUrl}/${id}`, params);
  }

  deleteAdmin(id: number): Observable<User> {
    return this.http.delete<User>(`${this.baseUrl}/${id}`);
  }

  getAdminList(): Observable<User[]> {
    return this.http.get<User[]>(`${this.baseUrl}/`);
  }
}

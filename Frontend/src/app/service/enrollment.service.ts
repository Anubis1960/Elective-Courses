
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Enrollment } from '../model/enrollment.model';

@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {

  private baseUrl = 'http://localhost:8080/enrollment';

  constructor(private http: HttpClient) { }

  getEnrollment(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createEnrollment(enrollment: Enrollment): Observable<any> {
    return this.http.post(this.baseUrl, enrollment);
  }

  updateEnrollment(id: number, enrollment: Enrollment): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, enrollment);
  }

  deleteEnrollment(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  getEnrollmentsList(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  exportEnrollments(): Observable<any> {
    return this.http.get(`${this.baseUrl}/export`);
  }
}
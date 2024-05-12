
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Enrollment } from '../model/enrollment.model';


@Injectable({
  providedIn: 'root'
})
export class EnrollmentService {

  private baseUrl = 'http://localhost:8080/enrollment';

  constructor(private http: HttpClient) { }

  getEnrollment(id: number): Observable<Enrollment> {
    return this.http.get<Enrollment>(`${this.baseUrl}/${id}`);
  }

  createEnrollment(student_id: number, course_id: number, priority: number): Observable<Enrollment> {
    const params = new HttpParams()
    .set('studentId', student_id)
    .set('courseId', course_id)
    .set('priority', priority);
    return this.http.post<Enrollment>(`${this.baseUrl}/`, params);
  }

  updateEnrollment(enrollment: Enrollment): Observable<Enrollment> {
    return this.http.put<Enrollment>(`${this.baseUrl}/${enrollment.id}`, enrollment);
  }

  deleteEnrollment(id: number): Observable<Enrollment> {
    return this.http.delete<Enrollment>(`${this.baseUrl}/${id}`);
  }

  getEnrollmentsList(): Observable<Enrollment[]> {
    return this.http.get<Enrollment[]>(`${this.baseUrl}/`);
  }

  exportEnrollments(): Observable<any> {
    return this.http.get(`${this.baseUrl}/export`);
  }
}
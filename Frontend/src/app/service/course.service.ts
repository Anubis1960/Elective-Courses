
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Course } from '../model/course.model';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  private baseUrl = 'http://localhost:8080/course';

  constructor(private http: HttpClient) { }

  getCourse(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createCourse(course: Course): Observable<any> {
    return this.http.post(`${this.baseUrl}/`, course);
  }

  updateCourse(id: number, course: Course): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, course);
  }

  deleteCourse(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  getCoursesList(): Observable<any> {
    return this.http.get(`${this.baseUrl}/`);
  }
}
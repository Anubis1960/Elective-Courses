
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CourseSchedule } from '../model/course-schedule.model';

@Injectable({
  providedIn: 'root'
})
export class CourseScheduleService {

  private baseUrl = 'http://localhost:8080/course-schedule';

  constructor(private http: HttpClient) { }

  getCourseSchedule(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createCourseSchedule(courseSchedule: CourseSchedule): Observable<any> {
    return this.http.post(this.baseUrl, courseSchedule);
  }

  updateCourseSchedule(id: number, courseSchedule: CourseSchedule): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, courseSchedule);
  }

  deleteCourseSchedule(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  getCourseSchedulesList(): Observable<any> {
    return this.http.get(this.baseUrl);
  }

  exportCourseSchedules(): Observable<any> {
    return this.http.get(`${this.baseUrl}/export`);
  }
}
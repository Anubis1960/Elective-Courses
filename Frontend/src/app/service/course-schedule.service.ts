
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CourseSchedule } from '../model/course-schedule.model';

@Injectable({
  providedIn: 'root'
})
export class CourseScheduleService {

  private baseUrl = 'http://localhost:8080/course-schedule';

  constructor(private http: HttpClient) { }

  getCourseSchedule(id: number): Observable<CourseSchedule> {
    return this.http.get<CourseSchedule>(`${this.baseUrl}/${id}`);
  }

  createCourseSchedule(courseSchedule: CourseSchedule): Observable<CourseSchedule> {
    return this.http.post<CourseSchedule>(`${this.baseUrl}/`, courseSchedule);
  }

  updateCourseSchedule(courseSchedule: CourseSchedule): Observable<CourseSchedule> {
    return this.http.put<CourseSchedule>(`${this.baseUrl}/${courseSchedule.id}`, courseSchedule);
  }

  exportCourseSchedules(id: number): Observable<any> {
    const params = new HttpParams()
      .set('id', id);
    return this.http.get(`${this.baseUrl}/export`, { params, responseType: 'blob' });
  }
}
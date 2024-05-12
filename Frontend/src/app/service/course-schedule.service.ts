
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

  getCourseSchedule(id: number): Observable<CourseSchedule> {
    return this.http.get<CourseSchedule>(`${this.baseUrl}/${id}`);
  }

  createCourseSchedule(course_schedule: CourseSchedule): Observable<CourseSchedule> {
    return this.http.post<CourseSchedule>(`${this.baseUrl}/`, course_schedule);
  }

  updateCourseSchedule(course_schedule: CourseSchedule): Observable<CourseSchedule> {
    return this.http.put<CourseSchedule>(`${this.baseUrl}/${course_schedule.id}`, course_schedule);
  }

  deleteCourseSchedule(id: number): Observable<CourseSchedule> {
    return this.http.delete<CourseSchedule>(`${this.baseUrl}/${id}`);
  }

  getCourseSchedulesList(): Observable<CourseSchedule> {
    return this.http.get<CourseSchedule>(`${this.baseUrl}/`);
  }

  exportCourseSchedules(): Observable<any> {
    return this.http.get(`${this.baseUrl}/export`);
  }
}
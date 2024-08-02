
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Course } from '../model/course.model';
import { CoursesCategory } from '../model/courses-category.model';

@Injectable({
  providedIn: 'root'
})
export class CourseService {

  private baseUrl = 'http://localhost:8080/course';

  constructor(private http: HttpClient) { }

  getCourse(id: number): Observable<Course> {
    return this.http.get<Course>(`${this.baseUrl}/${id}`);
  }

  createCourse(name: string, description: string, category: string, facultySection: string, maximumStudents: number, year: number, teacherName: string): Observable<Course> {
    const params = new HttpParams()
    .set('courseName', name)
    .set('description', description)
    .set('category', category)
    .set('facultySection', facultySection)
    .set('maxStudentsAllowed', maximumStudents)
    .set('year', year)
    .set('teacherName', teacherName);
    return this.http.post<Course>(`${this.baseUrl}/`, params);
  }

  updateCourse(id: number, name: string, description: string, category: string, facultySection: string, maximumStudents: number, year: number, teacherName: string): Observable<Course> {
    const params = new HttpParams()
    .set('courseName', name)
    .set('description', description)
    .set('category', category)
    .set('facultySection', facultySection)
    .set('maxStudentsAllowed', maximumStudents)
    .set('year', year)
    .set('teacherName', teacherName);
    return this.http.put<Course>(`${this.baseUrl}/${id}`, params);
  }

  deleteCourse(id: number): Observable<Course> {
    return this.http.delete<Course>(`${this.baseUrl}/${id}`);
  }

  getCoursesList(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.baseUrl}/`);
  }

  getCoursesByFacultyAndYear(faculty: string, year: number): Observable<Course[]> {
    const params = new HttpParams()
    .set('faculty', faculty)
    .set('year', year);
    return this.http.get<Course[]>(`${this.baseUrl}/${faculty}/${year}`);
  }

  getCoursesByStudentId(id: number): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.baseUrl}/student/${id}`);
  }

  getAcceptedCoursesByStudentId(id: number): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.baseUrl}/accepted/${id}`);
  }

  exportPDF(courseId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/export/${courseId}`, {responseType: 'blob'});
  }
  getAllFacultySections(): Observable<string[]>{
    return this.http.get<string[]>(`${this.baseUrl}/faculty-section`);
  }

  getPendingCoursesByStudentId(id: number): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.baseUrl}/pending/${id}`);
  }

  getAvailableCourses(id: number, year: number, facultySection: string, category: string): Observable<Course[]> {
    const params = new HttpParams()
    .set('courseId', id)
    .set('year', year)
    .set('facultySection', facultySection)
    .set('category', category);
    return this.http.get<Course[]>(`${this.baseUrl}/available`, {params});
  }

  getNumberOfCoursesPerCategory(): Observable<CoursesCategory[]>{
    return this.http.get<CoursesCategory[]>(`${this.baseUrl}/numberOfCoursesPerCategory`);
  }
}
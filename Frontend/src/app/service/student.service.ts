
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Student } from '../model/student.model';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private baseUrl = 'http://localhost:8080/student';

  constructor(private http: HttpClient) { }

  getStudent(id: number): Observable<Student> {
    return this.http.get<Student>(`${this.baseUrl}/${id}`);
  }

  createStudent(name: string, grade: number, faculty_section: string, year: number, email: string, password: string): Observable<Student> {
    const params = new HttpParams()
    .set('name', name)
    .set('grade', grade)
    .set('facultySection', faculty_section)
    .set('year', year)
    .set('email', email)
    .set('password', password);
    return this.http.post<Student>(`${this.baseUrl}/`, params);
  }

  updateStudent(id: number, name: string, grade: number, faculty_section: string, year: number, email: string, password: string): Observable<Student> {
    const params = new HttpParams()
    .set('name', name)
    .set('grade', grade)
    .set('facultySection', faculty_section)
    .set('year', year)
    .set('email', email)
    .set('password', password);
    return this.http.put<Student>(`${this.baseUrl}/${id}`, params);
  }

  deleteStudent(id: number): Observable<Student> {
    return this.http.delete<Student>(`${this.baseUrl}/${id}`);
  }

  getStudentsList(): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/`);
  }

  getStudentsByCourseId(courseId: number): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/course/${courseId}`);
  }

  getAcceptedStudentsByCourseId(courseId: number): Observable<Student[]> {
    return this.http.get<Student[]>(`${this.baseUrl}/accepted/${courseId}`);
  }

  export(bitOptions: number, extension: string, facultySection?: string, year?: number): Observable<any> {
    
    let params = new HttpParams()
    if (facultySection){
      params = params.set('facultySection', facultySection);
    }

    if (year){
      params = params.set('year', year);
    }

    params = params.set('bitOptions', bitOptions)
    params = params.set('extension', extension);

    return this.http.get(`${this.baseUrl}/export`, {params, responseType: 'blob'});
  }
}
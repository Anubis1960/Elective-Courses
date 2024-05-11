
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Student } from '../model/student.model';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private baseUrl = 'http://localhost:8080/student';

  constructor(private http: HttpClient) { }

  getStudent(id: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${id}`);
  }

  createStudent(name: string, grade: number, faculty_section: string, year: number, email: string, password: string): Observable<any> {
    return this.http.post(this.baseUrl, { name, grade, faculty_section, year, email, password });
  }

  updateStudent(id: number, name: string, grade: number, faculty_section: string, year: number, email: string, password: string): Observable<any> {
    return this.http.put(`${this.baseUrl}/${id}`, { name, grade, faculty_section, year, email, password });
  }

  deleteStudent(id: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/${id}`);
  }

  getStudentsList(): Observable<any> {
    return this.http.get(this.baseUrl);
  }
}
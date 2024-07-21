
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

  createEnrollment(student_id: number, course_id: number): Observable<Enrollment> {
    const params = new HttpParams()
    .set('studentId', student_id)
    .set('courseId', course_id);
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
  getStudentEnrollmentsList(studentId: number): Observable<Enrollment[]> {
    return this.http.get<Enrollment[]>(`${this.baseUrl}/student/${studentId}`);
  }
  exportEnrollments(): Observable<any> {
    return this.http.get(`${this.baseUrl}/export`);
  }

  updateAllEnrollments(enrollments: Enrollment[]): Observable<Enrollment[]> {
    return this.http.put<Enrollment[]>(`${this.baseUrl}/`, enrollments);
  }

  assignStudentsToCourse(): Observable<Enrollment[]> {
    return this.http.get<Enrollment[]>(`${this.baseUrl}/assign`);
  }

  exportEnrollmentsToPDF(includeYear: boolean, includeSection: boolean, includeCourseName: boolean, includeStudentName: boolean, includeTeacher: boolean, includeStudentMail: boolean, includeGrade: boolean, includeCategory: boolean, includeNumOfStudents: boolean, includeAVGGrade: boolean, extension: string, facultySection?: string, year?: string): Observable<any> {
    let params = new HttpParams();
    // console.log(facultySection);
    // console.log(year);
  
    if (facultySection) {
      params = params.set('facultySection', facultySection);
    }
    if (year) {
      params = params.set('year', year);
    }

    params = params.set('includeYear', includeYear);
    params = params.set('includeSection', includeSection);
    params = params.set('includeCourseName', includeCourseName);
    params = params.set('includeStudentName', includeStudentName);
    params = params.set('includeTeacher', includeTeacher);
    params = params.set('includeStudentMail', includeStudentMail);
    params = params.set('includeGrade', includeGrade);
    params = params.set('includeCategory', includeCategory);
    params = params.set('includeNumOfStudents', includeNumOfStudents);
    params = params.set('includeAVGGrade', includeAVGGrade);
    params = params.set('extension', extension);
  
    return this.http.get(`${this.baseUrl}/export`, { params, responseType: 'blob' });
  }
  

  reassignStudent(studentId: number, courseId: number, newCourseId: number): Observable<Enrollment> {
    const params = new HttpParams()
    .set('studentId', studentId)
    .set('courseId', courseId)
    .set('newCourseId', newCourseId);
    return this.http.put<Enrollment>(`${this.baseUrl}/reassign`, params);
  }
}
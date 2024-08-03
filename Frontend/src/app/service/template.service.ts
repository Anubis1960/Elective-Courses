import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Template } from '../model/template.model';

@Injectable({
  providedIn: 'root'
})
export class TemplateService {

  private baseUrl = 'http://localhost:8080/templates';

  constructor(private http: HttpClient) { }

  getTemplates(): Observable<Template> {
    return this.http.get<Template>(`${this.baseUrl}/`)
  }

  createTemplate(name: string, options: number, classFlag: string, year?: number, facultySection?: string): Observable<Template> {
    let params = new HttpParams()
    
    params = params.set('name', name);
    
    if (year) {
      params = params.set('year', year);
    }

    if (facultySection) {
      params = params.set('facultySection', facultySection);
    }

    params = params.set('classFlag', classFlag);

    params = params.set('options', options);
    
    return this.http.post<Template>(`${this.baseUrl}/`,  params );
  }

  deleteTemplate(id: number): Observable<Template> {
    return this.http.delete<Template>(`${this.baseUrl}/${id}`);
  }

  updateTemplate(id: number, name: string, options: number, classFlag: string, year?: number, facultySection?: string): Observable<Template> {
    let params = new HttpParams()
    
    params = params.set('name', name);
    
    if (year) {
      params = params.set('year', year);
    }

    if (facultySection) {
      params = params.set('facultySection', facultySection);
    }

    params = params.set('classFlag', classFlag)

    params = params.set('options', options);

    return this.http.put<Template>(`${this.baseUrl}/${id}`,  params );
  }

  getByClassFlag(classFlag: string): Observable<Template[]> {
    let params = new HttpParams()
    params = params.set('classFlag', classFlag);
    return this.http.get<Template[]>(`${this.baseUrl}/class-flag`, {params});
  }
}

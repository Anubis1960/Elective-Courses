import { HttpClient } from '@angular/common/http';
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

  createTemplate(name: string, year: number, facultySection: string, options: number): Observable<Template> {
    const params = {
      name,
      year,
      facultySection,
      options
    };
    
    return this.http.post<Template>(`${this.baseUrl}/`, null, { params });
  }
}

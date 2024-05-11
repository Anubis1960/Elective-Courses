import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CourseService } from '../../service/course.service';
import { Course } from '../../model/course.model';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrl: './course.component.css'
})
export class CourseComponent {
  courses: Course[] | undefined;
  constructor(private http: HttpClient, private courseService: CourseService) {
    this.courseService.getCoursesList().subscribe(data => {
      this.courses = data;
    });
  }
}

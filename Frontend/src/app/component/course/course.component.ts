import { Component,ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CourseService } from '../../service/course.service';
import { Course } from '../../model/course.model';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrl: './course.component.css'
})
export class CourseComponent  {
  courses !: Course[] | undefined;
  dataSource: any;
  displyedColumns: string[]=['id', 'name', 'description', 'category', 'facultySection', 'maximumStudentsAllowed','numberOfStudents','teacherName','year','action'];
  @ViewChild(MatPaginator) paginator !: MatPaginator;
  constructor(private http: HttpClient, private courseService: CourseService) {
    this.courseService.getCoursesList().subscribe(data => {
      this.courses = data;
      this.dataSource = new MatTableDataSource<Course>(this.courses);
      this.dataSource.paginator = this.paginator;
      console.log(this.courses);
    });
  }
}

import { Component,OnInit,ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CourseService } from '../../service/course.service';
import { Course } from '../../model/course.model';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrl: './course.component.css'
})
export class CourseComponent implements OnInit {

  courses !: Course[] | undefined;
  dataSource: any;
  displyedColumns: string[]=['id', 'name', 'description', 'category', 'facultySection', 'maximumStudentsAllowed','numberOfStudents','teacherName','year','action'];

  constructor(private http: HttpClient, private courseService: CourseService) { }

  @ViewChild(MatPaginator) paginator !: MatPaginator;
  @ViewChild(MatSort) sort !: MatSort;
  ngOnInit(): void {
    this.courseService.getCoursesList().subscribe({
      next: (data: Course[]) => {
        this.courses = data;
        this.dataSource = new MatTableDataSource<Course>(this.courses);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        console.log(this.courses);
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator
    this.dataSource.sort = this.sort;
  }

  onDelete(id: number) {
    this.courseService.deleteCourse(id).subscribe({
      next: (data: Course) => {
        console.log(data);
        this.courseService.getCoursesList().subscribe({
          next: (data: Course[]) => {
            this.courses = data;
            this.dataSource = new MatTableDataSource<Course>(this.courses);
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
            console.log(this.courses);
          },
          error: (error) => {
            console.log(error);
          }
        });
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
}

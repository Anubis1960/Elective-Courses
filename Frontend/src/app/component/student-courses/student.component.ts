import { Component, ViewChild } from '@angular/core';
import { Course } from '../../model/course.model';
import { HttpClient } from '@angular/common/http';
import { CourseService } from '../../service/course.service';
import { MatDialog } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';

@Component({
  selector: 'app-student',
  templateUrl: './student.component.html',
  styleUrls: ['./student.component.css']
})

export class StudentComponent {
  courses!: Course[] | undefined;
  dataSource: MatTableDataSource<Course> = new MatTableDataSource<Course>();
  displyedColumns: string[] = ['id', 'name', 'description', 'category', 'facultySection', 'maximumStudentsAllowed', 'numberOfStudents', 'teacherName', 'year'];

  constructor(private http: HttpClient, private courseService: CourseService, private dialog: MatDialog, private router: Router) { }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  ngOnInit(): void {
    const user = JSON.parse(sessionStorage.getItem('user') || '{}');
    console.log(user);
    this.courseService.getCoursesByStudentId(user.id).subscribe({
      next: (data: Course[]) => {
        this.courses = data;
        this.dataSource = new MatTableDataSource<Course>(this.courses);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  getDetails(id: number) {
    this.router.navigateByUrl('/student/courses/'+id);
  }
  filterchange(data:Event) {
    const value=(data.target as HTMLInputElement).value;
    this.dataSource.filter = value.trim().toLowerCase();
  }
}

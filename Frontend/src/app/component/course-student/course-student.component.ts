import { Component, ViewChild } from '@angular/core';
import { Course } from '../../model/course.model';
import { CourseService } from '../../service/course.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { StudentService } from '../../service/student.service';
import { Student } from '../../model/student.model';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-course-student',
  templateUrl: './course-student.component.html',
  styleUrl: './course-student.component.css'
})
export class CourseStudentComponent {
  courseId: number | undefined;
  students !: Student[] | undefined;
  dataSource: MatTableDataSource<Student> = new MatTableDataSource<Student>();
  displayedColumns: string[]=['id', 'name', 'email', 'facultySection', 'year','grade'];
  status: string = localStorage.getItem('status') || '';

  constructor(private snackbar: MatSnackBar,private route: ActivatedRoute, private httpClient: HttpClient, private studentService: StudentService, private router: Router) { }

  @ViewChild(MatPaginator) paginator !: MatPaginator;
  @ViewChild(MatSort) sort !: MatSort;
  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.courseId = Number(params.get('id'));
      if (!this.studentService) {
        this.studentService = new StudentService(this.httpClient);
      }
      
      if(this.status == 'true'){
        this.studentService.getStudentsByCourseId(this.courseId).subscribe({
          next: (data: Student[]) => {
            this.students = data;
            this.dataSource.data = this.students;
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
          },
          error: (error) => {
            this.snackbar.open('Error fetching data', undefined, {
              duration: 2000
            });
          }
        });
      }
      else{
        this.studentService.getAcceptedStudentsByCourseId(this.courseId).subscribe({
          next: (data: Student[]) => {
            this.students = data;
            this.dataSource.data = this.students;
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
        },
          error: (error) => {
            this.snackbar.open('Error fetching data', undefined, {
              duration: 2000
            });
          }
        });
      }
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator
    this.dataSource.sort = this.sort;
  }

  goToStudentDetails(id: number){
    this.router.navigate(['/admin/students', id]);
  }
}

import { Component, ViewChild } from '@angular/core';
import { Course } from '../../model/course.model';
import { CourseService } from '../../service/course.service';
import { ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { StudentService } from '../../service/student.service';
import { Student } from '../../model/student.model';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

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

  constructor(private route: ActivatedRoute, private httpClient: HttpClient, private studentService: StudentService) { }

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
            this.dataSource = new MatTableDataSource<Student>(this.students);
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
            //console.log(this.students);
          },
          error: (error) => {
            //console.log(error);
          }
        });
      }
      else{
        this.studentService.getAcceptedStudentsByCourseId(this.courseId).subscribe({
          next: (data: Student[]) => {
            this.students = data;
            this.dataSource = new MatTableDataSource<Student>(this.students);
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
            // console.log(this.students);
          },
          error: (error) => {
            //console.log(error);
          }
        });
      }
      //console.log(this.students);

    });
  }

  ngAfterViewInit() {
    console.log('status: '+this.status)
    this.dataSource.paginator = this.paginator
    this.dataSource.sort = this.sort;
  }
}

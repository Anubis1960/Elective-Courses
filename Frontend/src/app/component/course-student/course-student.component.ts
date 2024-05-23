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
  course: Course | undefined;
  courseService: CourseService | undefined;
  students !: Student[] | undefined;
  dataSource: MatTableDataSource<Student> = new MatTableDataSource<Student>();
  displyedColumns: string[]=['id', 'name', 'email', 'facultySection', 'year','grade'];

  constructor(private route: ActivatedRoute, private httpClient: HttpClient, private studentService: StudentService) { }

  @ViewChild(MatPaginator) paginator !: MatPaginator;
  @ViewChild(MatSort) sort !: MatSort;
  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const id = Number(params.get('id'));
      if (!this.courseService) {
        this.courseService = new CourseService(this.httpClient);
      }
      this.courseService.getCourse(id).subscribe({
        next: (data: Course) => {
          this.course = data;
          console.log(this.course);
        },
        error: (error) => {
          console.log(error);
        }
      });
      if (!this.studentService) {
        this.studentService = new StudentService(this.httpClient);
      }
      this.studentService.getStudentsByCourseId(id).subscribe({
        next: (data: Student[]) => {
          this.students = data;
          this.dataSource = new MatTableDataSource<Student>(this.students);
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
          console.log(this.students);
        },
        error: (error) => {
          console.log(error);
        }
      });

    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator
    this.dataSource.sort = this.sort;
  }
}

import { Component, OnInit, ViewChild } from '@angular/core';
import { Course } from '../../model/course.model';
import { ActivatedRoute } from '@angular/router';
import { CourseService } from '../../service/course.service';
import { HttpClient } from '@angular/common/http';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

@Component({
  selector: 'app-student-details',
  templateUrl: './student-details.component.html',
  styleUrl: './student-details.component.css'
})
export class StudentDetailsComponent implements OnInit{
  courses: Course[] | undefined;
  courseService: CourseService | undefined;
  id: number | undefined;
  dataSource: MatTableDataSource<Course> = new MatTableDataSource<Course>();
  displayedColumns: string[] = ['id', 'name', 'description', 'category', 'facultySection', 'maximumStudentsAllowed', 'numberOfStudents', 'teacherName', 'year'];

  constructor(private route: ActivatedRoute, private httpClient: HttpClient) { }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  ngOnInit(): void {
    this.route.paramMap.subscribe(params => {
      this.id = Number(params.get('id'));
      if (!this.courseService) {
        this.courseService = new CourseService(this.httpClient);
      }
      this.courseService.getCoursesByStudentId(this.id).subscribe({
        next: (data: Course[]) => {
          this.courses = data;
          //console.log(this.courses);
          this.dataSource.data = this.courses;
          this.dataSource.paginator = this.paginator;
          this.dataSource.sort = this.sort;
        },
        error: (error) => {
          //console.log(error);
        }
      });
    });
  }

  filterChange(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.dataSource.filter = value.trim().toLowerCase();
  }
  
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator
    this.dataSource.sort = this.sort;
  }

}

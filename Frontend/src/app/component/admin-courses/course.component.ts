import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CourseService } from '../../service/course.service';
import { Course } from '../../model/course.model';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { PopUpComponent } from '../pop-up/pop-up.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {

  courses!: Course[];
  dataSource: MatTableDataSource<Course> = new MatTableDataSource<Course>();
  displayedColumns: string[] = ['id', 'name', 'description', 'category', 'facultySection', 'maximumStudentsAllowed', 'numberOfStudents', 'teacherName', 'year', 'action'];

  constructor(private http: HttpClient, private courseService: CourseService, private dialog: MatDialog, private router: Router) { }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  
  ngOnInit(): void {
    this.refresh();
  }
  
  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  onDelete(id: number) {
    this.courseService.deleteCourse(id).subscribe({
      next: (data: Course) => {
        console.log(data);
        this.courses = this.courses.filter(course => course.id !== id);
        this.updateDataSource();
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  openDialog(course: Course | null, title: string) {
    const dialogRef = this.dialog.open(PopUpComponent, {
      width: '590px',
      height: '880px',
      data: {
        title: title,
        course: course
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (title === 'Edit Course') {
          const index = this.courses.findIndex(c => c.id === result.id);
          if (index !== -1) {
            this.courses[index] = result;
          }
        } else {
          this.courses.push(result);
        }
        this.updateDataSource();
      }
    });
  }

  refresh() {
    this.courseService.getCoursesList().subscribe({
      next: (data: Course[]) => {
        this.courses = data;
        this.updateDataSource();
      },
      error: (error) => {
        console.log(error);
      }
    });
  }

  getDetails(id: number) {
    this.router.navigateByUrl('/admin/courses/' + id);
  }

  filterChange(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.dataSource.filter = value.trim().toLowerCase();
  }

  private updateDataSource() {
    this.dataSource.data = this.courses;
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}

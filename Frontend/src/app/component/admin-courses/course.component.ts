import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CourseService } from '../../service/course.service';
import { Course } from '../../model/course.model';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { PopUpComponent } from '../course-pop-up/pop-up.component';
import { Router } from '@angular/router';
import { ApplicationPeriodService } from '../../service/application-period.service';

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {

  courses!: Course[];
  dataSource: MatTableDataSource<Course> = new MatTableDataSource<Course>();
  displayedColumns: string[] = ['id', 'name', 'description', 'category', 'facultySection', 'maximumStudentsAllowed', 'numberOfStudents', 'teacherName', 'year', 'action'];
  status: string = localStorage.getItem('status') || '';

  constructor(private http: HttpClient, private courseService: CourseService, private dialog: MatDialog, private router: Router, private applicationPeriodService: ApplicationPeriodService) { }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  
  ngOnInit(): void {
    console.log(this.status);
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

  openDialog(course: Course | null) {
    console.log(course);
    const dialogRef = this.dialog.open(PopUpComponent, {
      width: '590px',
      height: '880px',
      data: {
        id: course?.id,
        name: course?.name,
        description: course?.description,
        category: course?.category,
        facultySection: course?.facultySection,
        maximumStudentsAllowed: course?.maximumStudentsAllowed,
        year: course?.year,
        teacherName: course?.teacherName
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        if (course) {
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

  checkStatus() {
    console.log(this.status);
    return this.status == 'false';
  }

  closePeriod() {
    this.applicationPeriodService.reverseApplicationPeriodStatus().subscribe({
      next: (data) => {
        console.log(data);
        this.status = data.toString();
        localStorage.setItem('status', JSON.stringify(data));
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
}

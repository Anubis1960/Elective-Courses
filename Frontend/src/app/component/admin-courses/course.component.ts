import { Component, Input, OnInit, ViewChild } from '@angular/core';
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
import { EnrollmentService } from '../../service/enrollment.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CourseSchedule } from '../../model/course-schedule.model';
import { CourseScheduleService } from '../../service/course-schedule.service';
import { error } from 'console';
import { provideNativeDateAdapter } from '@angular/material/core';
import { DatePipe } from '@angular/common';
import { AdminService } from '../../service/admin.service';
import { MatSnackBar } from '@angular/material/snack-bar';


@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {
  @Input() courseId!: number | undefined;
  courses!: Course[];
  dataSource: MatTableDataSource<Course> = new MatTableDataSource<Course>();
  displayedColumns: string[] = ['name', 'category', 'facultySection', 'maximumStudentsAllowed', 'numberOfStudents', 'teacherName', 'year', 'action'];
  status: string = localStorage.getItem('status') ?? '';
  years: number[] = [1, 2, 3];
  facultySections: string[] | undefined;
  form!: FormGroup;
  scheduleDetails: { [key: number]: CourseSchedule } = {};
  filterValues: any = {};
  
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private http: HttpClient,
    private courseService: CourseService,
    private courseScheduleService: CourseScheduleService,
    private dialog: MatDialog,
    private router: Router,
    private enrollmentService: EnrollmentService,
    private fb: FormBuilder,
    private snackbar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.status = localStorage.getItem('status') || '';
    this.refresh();
    this.form = this.fb.group({
      facultySection: [''],
      year: [''],
      includeYear: [false],
      includeSection: [false],
      includeCourseName: [false],
      includeStudentName: [false],
      includeTeacher: [false],
      includeStudentMail: [false],
      includeGrade: [false],
      includeCategory: [false],
      includeNumOfStudents: [false],
      includeAVGGrade: [false],
      extension: ['', Validators.required]
    });

    this.getAllFacultySection();

    this.displayedColumns.forEach(column => {
      this.filterValues[column] = '';
    });

    this.dataSource.filterPredicate = this.createFilter();
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  getAllFacultySection(){
    this.courseService.getAllFacultySections().subscribe({
      next: (data: string[]) => {
        this.facultySections = data;
      },
      error: (error) => {
        this.snackbar.open('Error fetching data', undefined, {
          duration: 2000
        });
      }
    });
  }

  onDelete(id: number) {
    this.courseService.deleteCourse(id).subscribe({
      next: (data: Course) => {
        this.courses = this.courses.filter(course => course.id !== id);
        this.updateDataSource();
      },
      error: (error) => {
        this.snackbar.open('Error deleting course', undefined, {
          duration: 2000
        });
      }
    });
  }

  openDialog(course: Course | null) {
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
        this.dataSource.data = this.courses;
      }
    });
  }

  refresh() {
    this.courseService.getCoursesList().subscribe({
      next: (data: Course[]) => {
        this.courses = data;
        this.dataSource.data = this.courses;
      },
      error: (error) => {
        this.snackbar.open('Error refreshing', undefined, {
          duration: 2000
        });
      }
    });
  }
  
  getDetails(id: number) {
    this.router.navigate(['/admin/courses', id]);
  }

  displayScheduleDetails(courseId: number){
    this.courseScheduleService.getCourseSchedule(courseId).subscribe({
      next: (data:CourseSchedule) =>{
        this.scheduleDetails[courseId] = data;
      },
      error: (error)=>{
        this.snackbar.open('Error displaying schedule details', undefined, {
          duration: 2000
        });
      }
    });
  }

  filterChange(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.dataSource.filter = value.trim().toLowerCase();
  }

  applyColumnFilter(event: Event, column: string) {
    const filterValue = (event.target as HTMLInputElement).value;
    this.filterValues[column] = filterValue.trim().toLowerCase();
    this.dataSource.filter = JSON.stringify(this.filterValues);
  }

  createFilter() {
    return (data: any, filter: string): boolean => {
      const searchTerms = JSON.parse(filter);
      return Object.keys(searchTerms).every(column => {
        return searchTerms[column] === '' || data[column].toString().toLowerCase().indexOf(searchTerms[column]) !== -1;
      });
    };
  }

  private updateDataSource() {
    this.dataSource.data = this.courses;
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  checkStatus() {
    return this.status == 'false';
  }

  exportPDF() {
    const facultySection = this.form.get('facultySection')?.value;
    const year = this.form.get('year')?.value;
    const includeYear = this.form.get('includeYear')?.value;
    const includeSection = this.form.get('includeSection')?.value;
    const includeCourseName = this.form.get('includeCourseName')?.value;
    const includeStudentName = this.form.get('includeStudentName')?.value;
    const includeTeacher = this.form.get('includeTeacher')?.value;
    const includeStudentMail = this.form.get('includeStudentMail')?.value;
    const includeGrade = this.form.get('includeGrade')?.value;
    const includeCategory = this.form.get('includeCategory')?.value;
    const includeNumOfStudents = this.form.get('includeNumOfStudents')?.value;
    const includeAVGGrade = this.form.get('includeAVGGrade')?.value;
    const extension = this.form.get('extension')?.value;
    console.log(facultySection);
    console.log(year);
    this.enrollmentService.exportEnrollmentsToPDF(includeYear, includeSection, includeCourseName, includeStudentName, includeTeacher, includeStudentMail, includeGrade, includeCategory, includeNumOfStudents, includeAVGGrade, extension, facultySection, year).subscribe({
      next: (data) => {
        var fileType = 'application/pdf';
        if (extension === 'excel') {
          fileType = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet';
        }
        if (extension === 'csv') {
          fileType = 'text/csv';
        }
        const blob = new Blob([data], { type: fileType });
        const url = window.URL.createObjectURL(blob);
        window.open(url);
      },
      error: (error) => {
        this.snackbar.open('Error generating report', undefined, {
          duration: 2000
        });
      }
    });
  }
}

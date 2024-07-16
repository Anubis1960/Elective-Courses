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

@Component({
  selector: 'app-course',
  templateUrl: './course.component.html',
  styleUrls: ['./course.component.css']
})
export class CourseComponent implements OnInit {
  @Input() courseId!: number | undefined;
  courses!: Course[];
  dataSource: MatTableDataSource<Course> = new MatTableDataSource<Course>();
  displayedColumns: string[] = ['id', 'name', 'category', 'facultySection', 'maximumStudentsAllowed', 'numberOfStudents', 'teacherName', 'year', 'action'];
  status: string = localStorage.getItem('status') ?? '';
  years: number[] = [1, 2, 3];
  facultySections: string[] | undefined;
  form!: FormGroup;
  scheduleDetails: { [key: number]: CourseSchedule } = {};

  
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;

  constructor(
    private http: HttpClient,
    private courseService: CourseService,
    private courseScheduleService: CourseScheduleService,
    private dialog: MatDialog,
    private router: Router,
    private applicationPeriodService: ApplicationPeriodService,
    private enrollmentService: EnrollmentService,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    this.status = localStorage.getItem('status') || '';
    this.refresh();
    this.form = this.fb.group({
      facultySection: [''],
      year: [''],
      includeEnrollmentId: [false],
      includeStudentId: [false],
      includeCourseId: [false],
      includeYear: [false],
      includeSection: [false],
      includeCourseName: [false],
      includeStudentName: [false],
      includeTeacher: [false],
      includeStudentMail: [false],
      includeGrade: [false],
      includeCategory: [false],
      extension: ['', Validators.required]
    });
    this.courseService.getAllFacultySections().subscribe({
      next: (data: string[]) => {
        this.facultySections = data;
      },
      error: (error) => {
        //console.log(error);
      }
    });

  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  onDelete(id: number) {
    this.courseService.deleteCourse(id).subscribe({
      next: (data: Course) => {
        this.courses = this.courses.filter(course => course.id !== id);
        this.updateDataSource();
      },
      error: (error) => {
        //console.log(error);
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
        //console.log(error);
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
        //console.log(error);
      }
    });
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
    return this.status == 'false';
  }

  closePeriod() {
    this.applicationPeriodService.reverseApplicationPeriodStatus().subscribe({
      next: (data) => {
        this.status = data.toString();
        localStorage.setItem('status', JSON.stringify(data));
        this.enrollmentService.assignStudentsToCourse().subscribe({
          next: (data) => {
            //console.log(data);
          },
          error: (error) => {
            //console.log(error);
          }
        });
        this.refresh();
      },
      error: (error) => {
        //console.log(error);
      }
    });
  }

  exportPDF() {
    const facultySection = this.form.get('facultySection')?.value;
    const year = this.form.get('year')?.value;
    const includeEnrollmentId = this.form.get('includeEnrollmentId')?.value;
    const includeStudentId = this.form.get('includeStudentId')?.value;
    const includeCourseId = this.form.get('includeCourseId')?.value;
    const includeYear = this.form.get('includeYear')?.value;
    const includeSection = this.form.get('includeSection')?.value;
    const includeCourseName = this.form.get('includeCourseName')?.value;
    const includeStudentName = this.form.get('includeStudentName')?.value;
    const includeTeacher = this.form.get('includeTeacher')?.value;
    const includeStudentMail = this.form.get('includeStudentMail')?.value;
    const includeGrade = this.form.get('includeGrade')?.value;
    const includeCategory = this.form.get('includeCategory')?.value;
    const extension = this.form.get('extension')?.value;
    console.log(facultySection);
    console.log(year);
    this.enrollmentService.exportEnrollmentsToPDF(includeEnrollmentId, includeStudentId, includeCourseId, includeYear, includeSection, includeCourseName, includeStudentName, includeTeacher, includeStudentMail, includeGrade, includeCategory, extension, facultySection, year).subscribe({
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
        //console.log(error);
      }
    });
  }
}

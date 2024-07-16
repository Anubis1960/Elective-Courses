import { Component, ViewChild } from '@angular/core';
import { Student } from '../../model/student.model';
import { MatTableDataSource } from '@angular/material/table';
import { HttpClient } from '@angular/common/http';
import { StudentService } from '../../service/student.service';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { CourseService } from '../../service/course.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-admin-students',
  templateUrl: './admin-students.component.html',
  styleUrl: './admin-students.component.css'
})
export class AdminStudentsComponent {
  students!: Student[];
  dataSource: MatTableDataSource<Student> = new MatTableDataSource<Student>();
  displayedColumns: string[] = ['id', 'name', 'email', 'facultySection', 'year', 'grade'];
  years: number[] = [1, 2, 3];
  facultySections: string[] | undefined;
  form!: FormGroup;

  constructor(private http: HttpClient, private studentService: StudentService, private dialog: MatDialog, private router: Router, private courseService: CourseService, private fb: FormBuilder) { }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  ngOnInit(): void {
    this.courseService.getAllFacultySections().subscribe({
      next: (data) => {
        this.facultySections = data;
      },
      error: (error) => {
        //console.log(error);
      }
    });
    this.studentService.getStudentsList().subscribe({
      next: (data: Student[]) => {
        this.students = data;
        this.dataSource.data = this.students;
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      },
      error: (error) => {
        //console.log(error);
      }
    });

    this.form = this.fb.group({
      facultySection: [''],
      year: [''],
      includeId: [false],
      includeName: [false],
      includeGrade: [false],
      includeSection: [false],
      includeYear: [false],
      includeMail: [false],
      extension: ['', Validators.required]
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  getDetails(id: number) {
    this.router.navigate(['admin/students/' + id]);
  }

  filterChange(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.dataSource.filter = value.trim().toLowerCase();
  }

  exportPDF() {
    const facultySection = this.form.get('facultySection')?.value;
    const year = this.form.get('year')?.value;
    const includeId = this.form.get('includeId')?.value;
    const includeName = this.form.get('includeName')?.value;
    const includeGrade = this.form.get('includeGrade')?.value;
    const includeFacultySection = this.form.get('includeSection')?.value;
    const includeYear = this.form.get('includeYear')?.value;
    const includeEmail = this.form.get('includeMail')?.value;
    const extension = this.form.get('extension')?.value;

    this.studentService.export(includeId, includeName, includeGrade, includeFacultySection, includeYear, includeEmail, extension, facultySection, year).subscribe({
      next: (data) => {
        console.log(data);
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
        console.log(error);
      }
    });
  }


}

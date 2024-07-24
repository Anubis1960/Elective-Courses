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
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-admin-students',
  templateUrl: './admin-students.component.html',
  styleUrl: './admin-students.component.css'
})
export class AdminStudentsComponent {
  students!: Student[];
  dataSource: MatTableDataSource<Student> = new MatTableDataSource<Student>();
  displayedColumns: string[] = ['name', 'email', 'facultySection', 'year', 'grade'];
  years: number[] = [1, 2, 3];
  facultySections: string[] | undefined;
  form!: FormGroup;
  filterValues: any = {};

  constructor(private http: HttpClient, private studentService: StudentService, private dialog: MatDialog, private router: Router, private courseService: CourseService, private fb: FormBuilder, private snackbar: MatSnackBar) { }

  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  ngOnInit(): void {
    this.courseService.getAllFacultySections().subscribe({
      next: (data) => {
        this.facultySections = data;
      },
      error: (error) => {
        this.snackbar.open('Error fetching data', undefined, {
          duration: 2000
        });
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
        this.snackbar.open('Error fetching data', undefined, {
          duration: 2000
        });
      }
    });

    this.form = this.fb.group({
      facultySection: [''],
      year: [''],
      includeName: [false],
      includeGrade: [false],
      includeSection: [false],
      includeYear: [false],
      includeMail: [false],
      extension: ['', Validators.required]
    });

    this.displayedColumns.forEach(column => {
      this.filterValues[column] = '';
    });

    this.dataSource.filterPredicate = this.createFilter();
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
  
  exportPDF() {
    const facultySection = this.form.get('facultySection')?.value;
    const year = this.form.get('year')?.value;
    const includeName = this.form.get('includeName')?.value;
    const includeGrade = this.form.get('includeGrade')?.value;
    const includeFacultySection = this.form.get('includeSection')?.value;
    const includeYear = this.form.get('includeYear')?.value;
    const includeEmail = this.form.get('includeMail')?.value;
    const extension = this.form.get('extension')?.value;

    this.studentService.export(includeName, includeGrade, includeFacultySection, includeYear, includeEmail, extension, facultySection, year).subscribe({
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

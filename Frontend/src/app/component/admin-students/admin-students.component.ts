import { Component, ViewChild } from '@angular/core';
import { Student } from '../../model/student.model';
import { MatTableDataSource } from '@angular/material/table';
import { HttpClient } from '@angular/common/http';
import { StudentService } from '../../service/student.service';
import { TemplateService } from '../../service/template.service';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { MatSort } from '@angular/material/sort';
import { MatPaginator } from '@angular/material/paginator';
import { CourseService } from '../../service/course.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Template } from '../../model/template.model';
import { consumerMarkDirty } from '@angular/core/primitives/signals';

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
  templates: Template[] = [];

  constructor(private http: HttpClient, private studentService: StudentService, 
    private dialog: MatDialog, private router: Router, 
    private courseService: CourseService, 
    private fb: FormBuilder, private snackbar: MatSnackBar,
    private templateService: TemplateService) { }

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
      templateName: [''],
      exportType: ['custom'],
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

    this.templateService.getByClassFlag('STUDENT').subscribe({
      next: (data) => {
        this.templates = data;
      },
      error: (error) => {
        this.snackbar.open('Error fetching data', undefined, {
          duration: 2000
        });
      }
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
    const includeName = this.form.get('includeName')?.value ? 1 : 0;
    const includeGrade = this.form.get('includeGrade')?.value? 1 : 0;
    const includeFacultySection = this.form.get('includeSection')?.value? 1 : 0;
    const includeYear = this.form.get('includeYear')?.value? 1 : 0;
    const includeEmail = this.form.get('includeMail')?.value? 1 : 0;
    const extension = this.form.get('extension')?.value;

    let bitOptions = (includeName << 0) | (includeGrade << 1) | (includeFacultySection << 2) | (includeYear << 3) | (includeEmail << 4);

    this.studentService.export(bitOptions, extension, facultySection, year).subscribe({
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

  createTemplate(): void {
    const facultySection = this.form.get('facultySection')?.value;
    const year = this.form.get('year')?.value;
    const name = this.form.get('templateName')?.value;  
    const includeName = this.form.get('includeName')?.value ? 1 : 0;
    const includeGrade = this.form.get('includeGrade')?.value ? 1 : 0;
    const includeSection = this.form.get('includeSection')?.value ? 1 : 0;
    const includeYear = this.form.get('includeYear')?.value ? 1 : 0;
    const includeMail = this.form.get('includeMail')?.value ? 1 : 0;

    // Combine options into a single number
    const options = (includeName << 0) | (includeGrade << 1) | (includeSection << 2) | (includeYear << 3) | (includeMail << 4);

    this.templateService.createTemplate(name, options, 'STUDENT', year, facultySection).subscribe({
      next: (response) => {
        this.snackbar.open('Template created successfully', 'Close', { duration: 2000 });
        this.templates.push(response);
      },
      error: (error) => {
        this.snackbar.open('Error creating template', 'Close', { duration: 2000 });
      }
    });
  }

  onExportTypeChange(event: any) {
    const exportType = event.value;
    if (exportType === 'custom' || exportType === 'template') {
      this.form.get('extension')?.setValue(null);
      this.form.get('facultySection')?.setValue(null);
      this.form.get('year')?.setValue(null);
      this.form.get('includeName')?.setValue(false);
      this.form.get('includeGrade')?.setValue(false);
      this.form.get('includeSection')?.setValue(false);
      this.form.get('includeYear')?.setValue(false);
      this.form.get('includeMail')?.setValue(false);
      return;
    }

    if(exportType.options){
      this.form.get('includeName')?.setValue(exportType.options & 1);
      this.form.get('includeGrade')?.setValue(exportType.options & 2);
      this.form.get('includeSection')?.setValue(exportType.options & 4);
      this.form.get('includeYear')?.setValue(exportType.options & 8);
      this.form.get('includeMail')?.setValue(exportType.options & 16);
    }

    this.form.get('year')?.setValue(exportType.year);
    this.form.get('facultySection')?.setValue(exportType.facultySection);
  }

  deleteTemplate(id: number) {
    this.templateService.deleteTemplate(id).subscribe({
      next: (response) => {
        this.snackbar.open('Template deleted successfully', 'Close', { duration: 2000 });
        this.templates = this.templates.filter(template => template.id !== id);
      },
      error: (error) => {
        this.snackbar.open('Error deleting template', 'Close', { duration: 2000 });
      }
    });

  
    this.form.get('exportType')?.setValue('custom');
  }

}

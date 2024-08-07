import { Component, OnInit, ViewChild } from '@angular/core';
import { Course } from '../../model/course.model';
import { ActivatedRoute, Router } from '@angular/router';
import { CourseService } from '../../service/course.service';
import { HttpClient } from '@angular/common/http';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatDialog } from '@angular/material/dialog';
import { ReassignPopUpComponent } from '../reassign-pop-up/reassign-pop-up.component';
import { EnrollmentService } from '../../service/enrollment.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-student-details',
  templateUrl: './student-details.component.html',
  styleUrl: './student-details.component.css'
})
export class StudentDetailsComponent implements OnInit{
  courses!: Course[];
  courseService: CourseService | undefined;
  id: number | undefined;
  dataSource: MatTableDataSource<Course> = new MatTableDataSource<Course>();
  displayedColumns: string[] = ['id', 'name', 'description', 'category', 'facultySection', 'maximumStudentsAllowed', 'numberOfStudents', 'teacherName', 'year','action'];
  status: string = localStorage.getItem('status') || '';
  options: Course[] | undefined;

  constructor(private snackbar: MatSnackBar,private route: ActivatedRoute, private httpClient: HttpClient, private dialog: MatDialog, private enrollmentService: EnrollmentService, private router: Router) { }
  @ViewChild(MatPaginator) paginator!: MatPaginator;
  @ViewChild(MatSort) sort!: MatSort;
  ngOnInit(): void {
    this.status = localStorage.getItem('status') || '';
    this.route.paramMap.subscribe(params => {
      this.id = Number(params.get('id'));
      if (!this.courseService) {
        this.courseService = new CourseService(this.httpClient);
      }
      if (this.status == 'true'){
        this.courseService.getPendingCoursesByStudentId(this.id).subscribe({
          next: (data: Course[]) => {
            this.courses = data;
            this.dataSource.data = this.courses;
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
          },
          error: (error) => {
            this.snackbar.open('Error fetching data', undefined, {
              duration: 2000
            });
          }
        });
      }
      else{
        this.courseService.getAcceptedCoursesByStudentId(this.id).subscribe({
          next: (data: Course[]) => {
            this.courses = data;
            this.dataSource.data = this.courses;
            this.dataSource.paginator = this.paginator;
            this.dataSource.sort = this.sort;
          },
          error: (error) => {
            this.snackbar.open('Error fetching data', undefined, {
              duration: 2000
            });
          }
        });
      }
    });

  }

  filterChange(event: Event) {
    const value = (event.target as HTMLInputElement).value;
    this.dataSource.filter = value.trim().toLowerCase();
  }
  populateCourseOptions(id: number, year: number, facultySection: string, category: string){
    this.courseService?.getAvailableCourses(id, year, facultySection, category).subscribe({
      next: (data: Course[]) => {
        this.options = data;
      },
      error: (error) => {
        this.snackbar.open('Error fetching data', undefined, {
          duration: 2000
        });
      }
    });
  }

  ngAfterViewInit() {
    this.dataSource.paginator = this.paginator
    this.dataSource.sort = this.sort;
  }

  openDialog(course: Course) {
    let availableCourses: Course[];
    if (this.id && course.id && course.year && course.facultySection && course.category) {
      this.courseService?.getAvailableCourses(course.id, course.year ,course.facultySection ,course.category ).subscribe({
        next: (data: Course[]) => {
          availableCourses = data;
          console.log(availableCourses);
          const dialogRef = this.dialog.open(ReassignPopUpComponent, {
            width: '400px',
            height: '600px',
            data: {
              availableCourses: availableCourses,
            }
          });

          dialogRef.afterClosed().subscribe(result => {
            if(result && this.id && course.id){
              this.enrollmentService.reassignStudent(this.id, course.id, result.id).subscribe({
                next: (data) => {
                  const index = this.courses.findIndex(c => c.id === course.id);
                  console.log(index);
                  if (index !== -1 && this.courses) {
                    console.log(result);
                    this.courses[index] = result;
                  }
                  this.dataSource.data = this.courses;
                },
                error: (error) => {
                  this.snackbar.open('Error fetching data', undefined, {
                    duration: 2000
                  });
                }
              });
            }
          });
        },
        error: (error) => {
          this.snackbar.open('Error fetching data', undefined, {
            duration: 2000
          });
        }
      });
    }
  }

  goToStudentDetails(id: number){
    this.router.navigate(['/admin/courses/', id]);
  }
}

import { Component, OnInit } from '@angular/core';
import { Course } from '../../model/course.model';
import { ActivatedRoute } from '@angular/router';
import { CourseService } from '../../service/course.service';
import { Enrollment } from '../../model/enrollment.model';
import { EnrollmentService } from '../../service/enrollment.service';
import { HttpClient } from '@angular/common/http';
import { User } from '../../model/user.model';
import { MatSnackBar } from '@angular/material/snack-bar';
import { StudentService } from '../../service/student.service';
import { Student } from '../../model/student.model';
@Component({
  selector: 'app-course-details',
  templateUrl: './course-details.component.html',
  styleUrl: './course-details.component.css'
})
export class CourseDetailsComponent implements OnInit{
  status: string | undefined;
  courseId: number | undefined;
  studentColleagues: Student[] | undefined
  enrollmentService: EnrollmentService | undefined;
  constructor(private route: ActivatedRoute, private httpClient: HttpClient,private snackbar: MatSnackBar, private studentService: StudentService) { }

  ngOnInit() {
    this.status = localStorage.getItem('status') || '';
    this.route.paramMap.subscribe(params => {
      this.courseId = Number(params.get('id'));
    });
    if(this.status == 'false' && this.courseId){
      this.studentService.getAcceptedStudentsByCourseId(this.courseId).subscribe({
        next: (data: Student[]) => {
          this.studentColleagues = data;
        },
        error: (error) => {
          this.snackbar.open('Error fetching data', undefined, {
            duration: 2000
          });
        }
      });
    }

  }

  
  onEnroll() {
    let message: string;
    const user = JSON.parse(sessionStorage.getItem('user') || '{}') as User;
    if (!this.enrollmentService) {
      this.enrollmentService = new EnrollmentService(this.httpClient);
    }
      if (this.courseId && user.id) {
        this.enrollmentService.createEnrollment(user.id, this.courseId).subscribe({
          next: (data: Enrollment) => {
            message = "You have successfully enrolled in this course"
            this.snackbar.open(message, undefined, {
              duration: 2000
            });
          },
          error: (error) => {
            message = "You are already enrolled in this course";
            this.snackbar.open(message, undefined, {
              duration: 2000
            });
          }
        });
      }
  }

}

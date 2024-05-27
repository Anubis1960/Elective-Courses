import { Component, OnInit } from '@angular/core';
import { Course } from '../../model/course.model';
import { ActivatedRoute } from '@angular/router';
import { CourseService } from '../../service/course.service';
import { Enrollment } from '../../model/enrollment.model';
import { EnrollmentService } from '../../service/enrollment.service';
import { HttpClient } from '@angular/common/http';
import { User } from '../../model/user.model';
import { MatSnackBar } from '@angular/material/snack-bar';
@Component({
  selector: 'app-course-details',
  templateUrl: './course-details.component.html',
  styleUrl: './course-details.component.css'
})
export class CourseDetailsComponent implements OnInit{
  status: string = localStorage.getItem('status') || '';
  courseId: number | undefined;
  enrollmentService: EnrollmentService | undefined;
  constructor(private route: ActivatedRoute, private httpClient: HttpClient,private snackbar: MatSnackBar) { }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.courseId = Number(params.get('id'));
      //console.log(this.courseId);
    });
  }

  
  onEnroll() {
    let message: string;
    const user = JSON.parse(sessionStorage.getItem('user') || '{}') as User;
    console.log(user);
    if (!this.enrollmentService) {
      this.enrollmentService = new EnrollmentService(this.httpClient);
    }
      //console.log(user.id);
      //console.log(this.course.id);
      if (this.courseId && user.id) {
        this.enrollmentService.createEnrollment(user.id, this.courseId).subscribe({
          next: (data: Enrollment) => {
            message = "You have successfully enrolled in this course"
            this.snackbar.open(message, undefined, {
              duration: 2000
            });
            //console.log(data);
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

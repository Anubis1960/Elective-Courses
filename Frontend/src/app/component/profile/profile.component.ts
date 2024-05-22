import { Component, OnInit } from '@angular/core';
import { Student } from '../../model/student.model';
import { StudentService } from '../../service/student.service';
import { HttpClient } from '@angular/common/http';
import { User } from '../../model/user.model';
import { Enrollment } from '../../model/enrollment.model';
import { EnrollmentService } from '../../service/enrollment.service';
import { CdkDragDrop, CdkDropList, CdkDrag, moveItemInArray } from '@angular/cdk/drag-drop';
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit {
  title = 'Profile';
  student: Student | undefined;
  student_service: StudentService | undefined;
  enrollments_list: Enrollment[] = [];
  enrollment_service: EnrollmentService | undefined;
  event: Event | undefined;
  constructor(private httpClient: HttpClient) { }
  ngOnInit() {
    const user = JSON.parse(sessionStorage.getItem('user') || '{}') as User;
    console.log(user);
    if (!this.student_service) {
      this.student_service = new StudentService(this.httpClient);
    }
    if (!this.enrollment_service) {
      this.enrollment_service = new EnrollmentService(this.httpClient);
    }
    if (user.id && user.role == 'STUDENT') {
      console.log(user);
      this.student_service.getStudent(user.id).subscribe({
        next: (data: Student) => {
          this.student = data;
          console.log(this.student);
        },
        error: (error) => {
          console.log(error);
        }
      });
      this.enrollment_service.getStudentEnrollmentsList(user.id).subscribe({
        next: (data: Enrollment[]) => {
          this.enrollments_list = data;

          console.log(this.enrollments_list);
        },
        error: (error) => {
          console.log(error);
        }
      });
    }
  }
  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.enrollments_list, event.previousIndex, event.currentIndex);
    for (let i = 0; i < this.enrollments_list.length; i++) {
      console.log(this.enrollments_list[i]);
    }
  }
  saveOrder() {
    console.log('Save order');
    if (!this.enrollment_service) {
      this.enrollment_service = new EnrollmentService(this.httpClient);
    }
    this.enrollment_service.updateAllEnrollments(this.enrollments_list).subscribe({
      next: (data: Enrollment[]) => {
        this.enrollments_list = data;
        console.log(this.enrollments_list);
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
}

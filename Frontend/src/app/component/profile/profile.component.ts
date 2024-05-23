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
  enrollments_list: Enrollment[] = [];
  enrollment_service: EnrollmentService | undefined;
  event: Event | undefined;
  user: User | undefined;
  constructor(private httpClient: HttpClient) { }

  ngOnInit() {
    this.user = JSON.parse(sessionStorage.getItem('user') || '{}') as User;
    //console.log(this.user);
    if (!this.enrollment_service) {
      this.enrollment_service = new EnrollmentService(this.httpClient);
      if (this.user.id !== undefined) {
        this.enrollment_service.getStudentEnrollmentsList(this.user.id).subscribe({
          next: (data: Enrollment[]) => {
            this.enrollments_list = data;

            //console.log(this.enrollments_list);
          },
          error: (error) => {
            //console.log(error);
          }
        });
      }
    }
  }

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.enrollments_list, event.previousIndex, event.currentIndex);
    for (let i = 0; i < this.enrollments_list.length; i++) {
      //console.log(this.enrollments_list[i]);
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
        //console.log(this.enrollments_list);
      },
      error: (error) => {
        //console.log(error);
      }
    });
  }
}

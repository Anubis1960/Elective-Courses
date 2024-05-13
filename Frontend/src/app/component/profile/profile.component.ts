import { Component, OnInit } from '@angular/core';
import { Student } from '../../model/student.model';
import { StudentService } from '../../service/student.service';
import { HttpClient } from '@angular/common/http';
import { User } from '../../model/user.model';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
    title = 'Profile';
    student: Student | undefined;
    student_service: StudentService | undefined;
    constructor(private httpClient: HttpClient) {}

    ngOnInit() {
      const user = JSON.parse(localStorage.getItem('user') || '{}');
      console.log(user);
      if (!this.student_service) {
        this.student_service = new StudentService(this.httpClient);
      }
      if (user.id && user.role == 'STUDENT'){
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
      }
    }
  }

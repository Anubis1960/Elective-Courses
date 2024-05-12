import { Component, OnInit } from '@angular/core';
import { Student } from '../../model/student.model';
import { StudentService } from '../../service/student.service';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent implements OnInit{
  title = 'Profile';
  user = JSON.parse(localStorage.getItem('user') || '{}');
  student: Student | undefined;
  student_service: StudentService | undefined;
  constructor(private httpClient: HttpClient) {}

  ngOnInit() {
    if (!this.student_service) {
      this.student_service = new StudentService(this.httpClient);
    }
    this.student_service.getStudent(this.user.id).subscribe({
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

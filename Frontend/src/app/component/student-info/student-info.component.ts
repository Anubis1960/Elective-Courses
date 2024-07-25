import { Component, Input } from '@angular/core';
import { Student } from '../../model/student.model';
import { StudentService } from '../../service/student.service';

@Component({
  selector: 'app-student-info',
  templateUrl: './student-info.component.html',
  styleUrl: './student-info.component.css'
})
export class StudentInfoComponent {
  @Input() studentId!: number | undefined;
  student!: Student | undefined;

  constructor(private studentService: StudentService) { }
  
  ngOnInit() {
    if (this.studentId) {
      this.studentService.getStudent(this.studentId).subscribe({
        next: (data: Student) => {
          this.student = data;
        },
        error: (error) => {
          
        }
      });
    }
  }
}

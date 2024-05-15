import { Component,OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { CourseService } from '../../service/course.service';
@Component({
  selector: 'app-add-course-dialog',
  templateUrl: './add-course-dialog.component.html',
  styleUrl: './add-course-dialog.component.css'
})
export class AddCourseDialogComponent implements OnInit{
  constructor(private service: CourseService, private dialogRef: MatDialogRef<AddCourseDialogComponent>){
    
  }
  ngOnInit(): void {
    this.service.getCoursesList();
  }
}

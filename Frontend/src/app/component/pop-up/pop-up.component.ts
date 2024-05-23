import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CourseService } from '../../service/course.service';
import { Course } from '../../model/course.model';

@Component({
  selector: 'app-pop-up',
  templateUrl: './pop-up.component.html',
  styleUrls: ['./pop-up.component.css']
})
export class PopUpComponent implements OnInit {

  input: any;
  disabeld: boolean = false;
  form!: FormGroup;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any, private ref: MatDialogRef<PopUpComponent>, private builder: FormBuilder, private courseService: CourseService) { }

  ngOnInit(): void {
    console.log(this.data);
    this.input = this.data;
    this.form = this.builder.group({
      id: [this.input.course?.id],
      name: [this.input.course?.name],
      description: [this.input.course?.description],
      category: [this.input.course?.category],
      facultySection: [this.input.course?.facultySection],
      maximumStudentsAllowed: [this.input.course?.maximumStudentsAllowed],
      year: [this.input.course?.year],
      teacherName: [this.input.course?.teacherName]
    });
  }

  close() {
    this.ref.close();
  }

  checkTitle() {
    return this.input.title == 'Edit Course';
  }

  save() {
    if (this.checkTitle()) {
      this.courseService.updateCourse(this.form.value.id, this.form.value.name, this.form.value.description, this.form.value.category, this.form.value.facultySection, this.form.value.maximumStudentsAllowed, this.form.value.year, this.form.value.teacherName).subscribe({
        next: (data) => {
          console.log(data);
          this.ref.close(data);
        },
        error: (error) => {
          console.log(error);
        }
      });
    }
    else {
      this.courseService.createCourse(this.form.value.name, this.form.value.description, this.form.value.category, this.form.value.facultySection, this.form.value.maximumStudentsAllowed, this.form.value.year, this.form.value.teacherName).subscribe({
        next: (data : Course) => {
          console.log(data);
          this.ref.close(data);
        },
        error: (error) => {
          console.log(error);
        }
      });
    }
  }

}
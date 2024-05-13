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
    this.input = this.data;
    this.form = this.builder.group({
      id: [this.input.id],
      name: [this.input.name],
      description: [this.input.description],
      category: [this.input.category],
      facultySection: [this.input.facultySection],
      maximumStudentsAllowed: [this.input.maximumStudentsAllowed],
      teacherName: [this.input.teacherName],
      year: [this.input.year]
    });
  }

  close() {
    this.ref.close();
  }

  checkTitle() {
    return this.input.title == 'Update Course';
  }

  save() {
    if (this.checkTitle()) {
      this.courseService.updateCourse(this.form.value.id, this.form.value.name, this.form.value.description, this.form.value.category, this.form.value.facultySection, this.form.value.maximumStudentsAllowed, this.form.value.year, this.form.value.teacherName).subscribe({
        next: (data) => {
          console.log(data);
          this.ref.close();
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
          this.ref.close();
        },
        error: (error) => {
          console.log(error);
        }
      });
    }
  }

}

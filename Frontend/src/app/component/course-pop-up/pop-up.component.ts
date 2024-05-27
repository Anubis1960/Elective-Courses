import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CourseService } from '../../service/course.service';
import { Course } from '../../model/course.model';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-pop-up',
  templateUrl: './pop-up.component.html',
  styleUrls: ['./pop-up.component.css']
})
export class PopUpComponent implements OnInit {

  input: Course | undefined;
  disabeld: boolean = false;
  form!: FormGroup;
  facultySections: string[] | undefined;
  years: number[]=[1,2,3];
  constructor(@Inject(MAT_DIALOG_DATA) public data: Course, private ref: MatDialogRef<PopUpComponent>, private builder: FormBuilder, private courseService: CourseService, private snackbat: MatSnackBar) { }

  ngOnInit(): void {
    console.log(this.data);
    this.input = this.data;
    this.form = this.builder.group({
      id: [this.input.id],
      name: [this.input.name],
      description: [this.input.description],
      category: [this.input.category],
      facultySection: [this.input.facultySection],
      maximumStudentsAllowed: [this.input.maximumStudentsAllowed],
      year: [this.input.year],
      teacherName: [this.input.teacherName]
    });

    const contentHeight = 320 + (Object.keys(this.input).length - 1) * 80;
    const contentWidth = 590;
    this.ref.updateSize(contentWidth + 'px', contentHeight + 'px');
    this.courseService.getAllFacultySections().subscribe({
      next: (data: string[]) => {
        this.facultySections = data;
      },
      error: (error) => {
        console.log(error);
      }
    })
    
  }

  close() {
    this.ref.close();
  }

  checkTitle() {
    return this.input?.id === undefined;
  }

  save() {
    if (this.checkTitle()) {
      this.courseService.updateCourse(this.form.value.id, this.form.value.name, this.form.value.description, this.form.value.category, this.form.value.facultySection, this.form.value.maximumStudentsAllowed, this.form.value.year, this.form.value.teacherName).subscribe({
        next: (data) => {
          //console.log(data);
          this.ref.close(data);
        },
        error: (error) => {
          //console.log(error);
          //const message = error.error.message;
          this.snackbat.open("Error updating course", undefined, {
            duration: 2000
          });
          
        }
      });
    }
    else {
      this.courseService.createCourse(this.form.value.name, this.form.value.description, this.form.value.category, this.form.value.facultySection, this.form.value.maximumStudentsAllowed, this.form.value.year, this.form.value.teacherName).subscribe({
        next: (data : Course) => {
          //console.log(data);
          this.ref.close(data);
        },
        error: (error) => {
          //console.log(error);
          //const message = error.error.message;
          this.snackbat.open("Error creating course", undefined, {
            duration: 2000
          });
          
        }
      });
    }
  }

}
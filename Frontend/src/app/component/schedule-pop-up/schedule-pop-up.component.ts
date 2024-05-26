import { Component, Inject, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CourseSchedule } from '../../model/course-schedule.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CourseScheduleService } from '../../service/course-schedule.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-schedule-pop-up',
  templateUrl: './schedule-pop-up.component.html',
  styleUrls: ['./schedule-pop-up.component.css']
})
export class SchedulePopUpComponent implements OnInit {
  input: CourseSchedule | undefined;
  form!: FormGroup;
  daysOfWeek: string[] = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY'];

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: CourseSchedule,
    private ref: MatDialogRef<SchedulePopUpComponent>,
    private builder: FormBuilder,
    private courseScheduleService: CourseScheduleService,
    private snackBar: MatSnackBar
  ) {}

  ngOnInit() {
    this.input = this.data;
    this.form = this.builder.group({
      id: [this.input?.id],
      day: [this.input?.day, Validators.required],
      startTime: [this.input?.startTime, [Validators.required, Validators.pattern('^([01]?[0-9]|2[0-3]):[0-5][0-9]$')]],
      endTime: [this.input?.endTime, [Validators.required, Validators.pattern('^([01]?[0-9]|2[0-3]):[0-5][0-9]$')]]
    });
  }

  close() {
    this.ref.close();
  }

  checkTitle() {
    return this.input?.day === undefined;
  }

  save() {
    if (this.form.valid) {
      const courseSchedule = new CourseSchedule(
        this.form.value.id,
        this.form.value.startTime,
        this.form.value.endTime,
        this.form.value.day
      );

      if (this.checkTitle()) {
        this.courseScheduleService.createCourseSchedule(courseSchedule).subscribe({
          next: (data) => {
            this.ref.close(data);
          },
          error: () => {
            this.snackBar.open("Error creating course schedule", undefined, {
              duration: 2000
            });
          }
        });
      } else {
        this.courseScheduleService.updateCourseSchedule(courseSchedule).subscribe({
          next: (data) => {
            this.ref.close(data);
          },
          error: () => {
            this.snackBar.open("Error updating course schedule", undefined, {
              duration: 2000
            });
          }
        });
      }
    }
  }
}

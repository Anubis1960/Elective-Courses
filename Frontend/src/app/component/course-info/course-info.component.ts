import { Component, Input } from '@angular/core';
import { Course } from '../../model/course.model';
import { CourseService } from '../../service/course.service';
import { CourseSchedule } from '../../model/course-schedule.model';
import { CourseScheduleService } from '../../service/course-schedule.service';
import { MatDialog } from '@angular/material/dialog';
import { SchedulePopUpComponent } from '../schedule-pop-up/schedule-pop-up.component';
import { ApplicationPeriodService } from '../../service/application-period.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-course-info',
  templateUrl: './course-info.component.html',
  styleUrl: './course-info.component.css'
})
export class CourseInfoComponent {
  @Input() courseId!: number | undefined;
  course!: Course | undefined;
  courseSchedule: CourseSchedule | undefined;
  role: string = JSON.parse(sessionStorage.getItem('user') || '{}').role;
  status: string | undefined;
  constructor(private snackbar: MatSnackBar, private dialog: MatDialog, private courseService: CourseService, private courseScheduleService: CourseScheduleService) { }

  ngOnInit() {
    this.status = localStorage.getItem('status') || '';
    const role = JSON.parse(sessionStorage.getItem('user') || '{}').role;
    //console.error(this.status);
    if (this.courseId) {
      this.courseService.getCourse(this.courseId).subscribe({
        next: (data: Course) => {
          this.course = data;
        },
        error: (error) => {
          this.course = undefined;
          this.snackbar.open('Course doesn\'t exist', undefined, {
            duration: 2000
          });
        }
      });
      this.courseScheduleService.getCourseSchedule(this.courseId).subscribe({
        next: (data: CourseSchedule) => {
          this.courseSchedule = data;
        },
        error: (error) => {
          this.courseSchedule = undefined;
        }
      });
    }
  }

  openDialog() {
    const dialogRef = this.dialog.open(SchedulePopUpComponent, {
      width: '590px',
      height: '880px',
      data: {
        id: this.course?.id,
        day: this.courseSchedule?.day,
        startTime: this.courseSchedule?.startTime,
        endTime: this.courseSchedule?.endTime
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.courseSchedule = result;
      }
    });
  }

  exportPDF(){
    if (!this.courseId) {
      return;
    }
    this.courseService.exportPDF(this.courseId).subscribe({
      next: (data: Blob) => {
        const blob = new Blob([data], { type: 'application/pdf' });
        const url = window.URL.createObjectURL(blob);
        window.open(url);
      },
      error: (error) => {
        this.snackbar.open('Error exporting pdf', undefined, {
          duration: 2000
        });
      }
    });
  }

}

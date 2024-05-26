import { Component, Input } from '@angular/core';
import { Course } from '../../model/course.model';
import { CourseService } from '../../service/course.service';
import { CourseSchedule } from '../../model/course-schedule.model';
import { CourseScheduleService } from '../../service/course-schedule.service';

@Component({
  selector: 'app-course-info',
  templateUrl: './course-info.component.html',
  styleUrl: './course-info.component.css'
})
export class CourseInfoComponent {
  @Input() courseId!: number | undefined;
  course!: Course | undefined;
  courseSchedule: CourseSchedule | undefined;

  constructor(private courseService: CourseService, private courseScheduleService: CourseScheduleService) { }

  ngOnInit() {
    if (this.courseId) {
      this.courseService.getCourse(this.courseId).subscribe({
        next: (data: Course) => {
          this.course = data;
        },
        error: (error) => {
          //console.log(error);
        }
      });
      this.courseScheduleService.getCourseSchedule(this.courseId).subscribe({
        next: (data: CourseSchedule) => {
          this.courseSchedule = data;
        },
        error: (error) => {
          //console.log(error);
          this.courseSchedule = undefined;
        }
      });
    }
  }

}

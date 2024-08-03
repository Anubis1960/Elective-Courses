import { Component, OnInit } from '@angular/core';
import { CourseService } from '../../service/course.service';
import { StudentService } from '../../service/student.service';
import { CategoryCount } from '../../model/category-count.model';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})

export class AdminHomeComponent implements OnInit{

  public numberOfCourses: number = 0;
  public numberOfStudents: number = 0;
  public numberOfCategories: number = 0;
  public categoryCount: CategoryCount[] = [];    
  
  constructor(
    private courseService: CourseService,
    private studentService: StudentService,
  ){}

  ngOnInit():void{
    this.getNumberOfCourses();
    this.getNumberOfStudents();
    this.processCourseData();
  }
  
  getNumberOfCourses(){
    this.courseService.getCoursesList().subscribe({
      next: (courses) => {
        this.numberOfCourses = courses.length;
      },
      error: (error) => {
      }
    })
  }

  getNumberOfStudents(){
    this.studentService.getStudentsList().subscribe({
      next: (students)=>{
        this.numberOfStudents = students.length;
      },
      error: (error) =>{
      }
    })
  }

  processCourseData(): void {
    this.courseService.getNumberOfCoursesPerCategory().subscribe({
      next: (categories) => {
        this.categoryCount = categories;
      },
      error: (err) => {
      }
    });
  }

}

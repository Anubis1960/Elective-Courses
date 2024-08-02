import { Component, OnInit } from '@angular/core';
import { CourseService } from '../../service/course.service';
import { error } from 'console';
import { StudentService } from '../../service/student.service';
import { Course } from '../../model/course.model';
import { CoursesCategory } from '../../model/courses-category.model';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})

export class AdminHomeComponent implements OnInit{

  public numberOfCourses: number = 0;
  public numberOfStudents: number = 0;
  public numberOfCourseCategories: number = 0;
  public categoryData: CoursesCategory[] = [];    
  
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
        console.log("a mers maaaa");
      },
      error: (error) => {
        console.error(error);
        console.log("Eroare ba");
      }
    })
 }
  getNumberOfStudents(){
    this.studentService.getStudentsList().subscribe({
      next: (students)=>{
        this.numberOfStudents = students.length;
        console.log("A mers baaaa")
      },
      error: (error) =>{
        console.error(error);
        console.log("Eroare ba");
      }
    })
  }
  processCourseData(): void {
    this.courseService.getNumberOfCoursesPerCategory().subscribe({
      next: (categories) => {
        this.categoryData = categories;
        console.log("Sunt in processCourseData");
        
      },
      error: (err) => {
        console.error('Error fetching course data:', err);
      }
    });
  }
}

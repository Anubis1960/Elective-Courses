import { Component, OnInit } from '@angular/core';
import { CourseService } from '../../service/course.service';
import { error } from 'console';
import { StudentService } from '../../service/student.service';
import { Course } from '../../model/course.model';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})

export class AdminHomeComponent implements OnInit{

  public numberOfCourses: number = 0;
  public numberOfStudents: number = 0;
  public numberOfCourseCategories: number = 0;
  public categoryData: { labels: string[], data: number[] } = { labels: [], data: [] };    
  
  constructor(
    private courseService: CourseService,
    private studentService: StudentService,
  ){}

  ngOnInit():void{
    this.getNumberOfCourses();
    this.getNumberOfStudents();
    this.courseService.getCoursesList().subscribe(courses => {
      this.categoryData = this.processCourseData(courses);
    });
    
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
  processCourseData(courses: Course[]): { labels: string[], data: number[] } {
    const categoryCounts: { [key: string]: number } = {};
    courses.forEach(course => {
      if (course.category) {
        categoryCounts[course.category] = (categoryCounts[course.category] || 0) + 1;
      }
    });
    const labels = Object.keys(categoryCounts);
    const data = Object.values(categoryCounts);

    return { labels, data };
  }
}

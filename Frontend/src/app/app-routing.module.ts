import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { StudentComponent } from './component/student/student.component';
import { CourseComponent } from './component/course/course.component';
import { CourseDetailsComponent } from './component/course-details/course-details.component';
import { ProfileComponent } from './component/profile/profile.component';
import { CourseStudentComponent } from './component/course-student/course-student.component';

const routes: Routes = [
  {path: '', redirectTo:'/login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'student/courses', component: StudentComponent},
  {path: 'admin/courses', component: CourseComponent},
  {path: 'student/courses/:id', component: CourseDetailsComponent},
  {path: 'profile', component: ProfileComponent},
  {path: 'admin/courses/:id', component: CourseStudentComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

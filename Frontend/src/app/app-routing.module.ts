import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './component/login/login.component';
import { StudentComponent } from './component/student-courses/student.component';
import { CourseComponent } from './component/admin-courses/course.component';
import { CourseDetailsComponent } from './component/course-details/course-details.component';
import { ProfileComponent } from './component/profile/profile.component';
import { CourseStudentComponent } from './component/course-student/course-student.component';
import { canActivateAdminGuard, canActivateStudentGuard } from './guard/auth-guard';
import { AdminStudentsComponent } from './component/admin-students/admin-students.component';
import { StudentDetailsComponent } from './component/student-details/student-details.component';
import { AdminHomeComponent } from './component/admin-home/admin-home.component';

const routes: Routes = [
  {path: '', redirectTo:'/login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'student/courses', component: StudentComponent, canActivate: [canActivateStudentGuard]},
  {path: 'admin/courses', component: CourseComponent, canActivate: [canActivateAdminGuard]},
  {path: 'student/courses/:id', component: CourseDetailsComponent, canActivate: [canActivateStudentGuard]},
  {path: 'profile', component: ProfileComponent, canActivate: [canActivateStudentGuard]},
  {path: 'admin/courses/:id', component: CourseStudentComponent, canActivate: [canActivateAdminGuard]},
  {path: 'admin/students', component: AdminStudentsComponent, canActivate: [canActivateAdminGuard]},
  {path: 'admin/students/:id', component: StudentDetailsComponent, canActivate: [canActivateAdminGuard]},
  {path: 'admin/home', component: AdminHomeComponent, canActivate: [canActivateAdminGuard]}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

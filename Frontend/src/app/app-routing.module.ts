import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { StudentComponent } from './student/student.component';

const routes: Routes = [
  {path: '', redirectTo:'/login', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'student', component: StudentComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

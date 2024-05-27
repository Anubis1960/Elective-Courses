import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginService } from '../../service/login.service';
import { User } from '../../model/user.model';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { ApplicationPeriodService } from '../../service/application-period.service';
import { app } from '../../../../server';
import { stat } from 'fs';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent { 
  email: string;
  password: string;
  user: User | undefined;

  constructor(private http: HttpClient, private loginService: LoginService, private router: Router, private snackbar: MatSnackBar, private applicationPeriodService: ApplicationPeriodService) { 
    this.email = '';
    this.password = '';
  }

  onLogin(){
    this.loginService.getUser(this.email, this.password).subscribe({
      next: (data: User) => {
        this.user = data;
        //console.log(this.user);
        sessionStorage.setItem('user', JSON.stringify(this.user));
        if(this.user.role === 'ADMIN'){
          this.router.navigateByUrl('/admin/courses');
        } else if(this.user.role === 'STUDENT'){
          this.router.navigateByUrl('/student/courses');
        }
        this.applicationPeriodService.getApplicationPeriodStatus().subscribe({
          next: (data: boolean) => {
            sessionStorage.setItem('status', data.toString());
          },
          error: (error) => {
            //console.log(error);
          }
        });
      },
      error: (error) => {
        //console.log(error);
        this.snackbar.open('Invalid email or password', undefined, {
          duration: 2000
        });
      }
    });
  }
}

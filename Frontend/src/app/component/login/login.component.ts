import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginService } from '../../service/login.service';
import { User } from '../../model/user.model';
import { Router } from '@angular/router';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent { 
  email: string;
  password: string;
  user: User | undefined;
  constructor(private http: HttpClient, private loginService: LoginService,private router: Router) { 
    this.email = '';
    this.password = '';
  }
  onLogin(){
    this.loginService.getUser(this.email, this.password).subscribe(
    {
      next: (data: User) => {
        this.user = data;
        console.log(this.user);
        localStorage.setItem('user', JSON.stringify(this.user));
        if(this.user.role === 'ADMIN'){
          this.router.navigateByUrl('/course');
        }
        else if(this.user.role === 'STUDENT'){
          this.router.navigateByUrl('/student');
        }
      },
      error: (error) => {
        console.log(error);
      }
    }
  );
  }
}
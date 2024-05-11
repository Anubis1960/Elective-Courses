import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { LoginService } from '../../service/login.service';
import { User } from '../../model/user.model';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent { 
  email: string;
  password: string;
  user: User | undefined;
  constructor(private http: HttpClient, private loginService: LoginService) {
    this.email = '';
    this.password = '';
  }
  onLogin(){
    this.loginService.getUser(this.email, this.password).subscribe(
    (response) => {
      this.user = response;
      console.log(this.user)
    },
    (error) => {
      console.log("Loggin failed",error);
    }
  );
  }
}
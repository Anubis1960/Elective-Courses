import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../../model/user.model';
@Component({
  selector: 'app-top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css']
})
export class TopBarComponent {
  userRole = JSON.parse(localStorage.getItem('user') || '{}').role;
  constructor(private router:Router ) { }
  onLogout(){
    console.log(localStorage.getItem('user'));
    const user = JSON.parse(localStorage.getItem('user') || '{}');
    console.log(user.id);    
    console.log(user.role);
    localStorage.removeItem('user');
    this.router.navigateByUrl('/login');
  } 
}

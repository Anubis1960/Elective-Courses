import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../../model/user.model';
@Component({
  selector: 'app-top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css']
})
export class TopBarComponent {
  constructor(private router:Router ) { }
  onLogout(){
    localStorage.removeItem('user');
    this.router.navigateByUrl('/login');

  }
}

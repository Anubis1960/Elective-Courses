import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../../model/user.model';
import { ApplicationPeriodService } from '../../service/application-period.service';
@Component({
  selector: 'app-top-bar',
  templateUrl: './top-bar.component.html',
  styleUrls: ['./top-bar.component.css']
})
export class TopBarComponent {
  userRole = JSON.parse(sessionStorage.getItem('user') || '{}').role;
  constructor(private router:Router, private applicationPeriodService: ApplicationPeriodService ) { }
  onLogout() {
    sessionStorage.removeItem('user');
    this.router.navigateByUrl('/login');
  } 

}

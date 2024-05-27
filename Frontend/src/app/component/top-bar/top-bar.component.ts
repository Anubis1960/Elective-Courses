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
  status = JSON.parse(localStorage.getItem('status') || '{}');

  constructor(private router:Router, private applicationPeriodService: ApplicationPeriodService ) { }
  
  onLogout() {
    sessionStorage.removeItem('user');
    this.router.navigateByUrl('/login');
  } 

  closePeriod() {
    this.applicationPeriodService.reverseApplicationPeriodStatus().subscribe({
      next: (data) => {
        console.log(data);
        this.status = data;
        localStorage.setItem('status', JSON.stringify(data));
      },
      error: (error) => {
        console.log(error);
      }
    });
  }
}

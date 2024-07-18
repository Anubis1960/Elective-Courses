import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { provideNativeDateAdapter } from '@angular/material/core';
import { ApplicationPeriodService } from '../../service/application-period.service';
import { EnrollmentService } from '../../service/enrollment.service';
import { Router } from '@angular/router';
import { DatePipe } from '@angular/common';

export const DATE_FORMATS = {
  parse: {
    dateInput: 'YYYY/MM/DD',
  },
  display: {
    dateInput: 'YYYY/MM/DD',
    monthYearLabel: 'MM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MM YYYY',
  },
};
@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  providers: [
    provideNativeDateAdapter(DATE_FORMATS), DatePipe,
  ],
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
  status: string | undefined;
  openPeriodForm!: FormGroup;

  constructor(
    private applicationPeriodService: ApplicationPeriodService,
    private enrollmentService: EnrollmentService,
    private fb: FormBuilder,
    private router:Router,
  ) {}

  ngOnInit(): void {
    this.status = localStorage.getItem('status') || '';
    this.openPeriodForm = this.fb.group({
      endDate: ['', Validators.required]
    });
  }

  closePeriod() {
    this.applicationPeriodService.reverseApplicationPeriodStatus().subscribe({
      next: (data) => {
        this.status = data.toString();
        localStorage.setItem('status', JSON.stringify(data));
        this.enrollmentService.assignStudentsToCourse().subscribe({
          next: (data) => {
            // console.log(data);
            location.reload();
          },
          error: (error) => {
            // console.log(error);
          }
        });
        
      },
      error: (error) => {
        // console.log(error);
      }
    });
  }

  openPeriod() {
    const date = this.openPeriodForm.get('endDate')?.value;
    const formatedDate = new DatePipe('en-US').transform(date, 'yyyy-MM-dd');
    if (formatedDate) {
      this.applicationPeriodService.setApplicationPeriod(formatedDate).subscribe({
        next: (data) => {
          location.reload();
        },
        error: (error) => {
          // console.log(error);
        }
      });
    }
  }
  onLogout() {
    sessionStorage.removeItem('user');
    this.router.navigateByUrl('/login');
  } 
}

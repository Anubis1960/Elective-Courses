import { Component, Inject, PLATFORM_ID } from '@angular/core';
import { ApplicationPeriodService } from './service/application-period.service';
import { isPlatformBrowser } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Frontend';

  constructor(
    private applicationPeriodService: ApplicationPeriodService,
    @Inject(PLATFORM_ID) private platformId: Object
  ) {
    this.applicationPeriodService.getApplicationPeriodStatus().subscribe({
      next: (data) => {
        console.log(data);
        if (isPlatformBrowser(this.platformId)) {
            localStorage.setItem('status', data.toString());
        }
      }
    });
  }
}

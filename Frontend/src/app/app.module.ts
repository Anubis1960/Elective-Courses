import { NgModule } from '@angular/core';
import { BrowserModule, provideClientHydration } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './component/login/login.component';
import { StudentComponent } from './component/student-courses/student.component';
import { FormsModule } from '@angular/forms';
import { HttpClientModule, provideHttpClient, withFetch } from '@angular/common/http';
import { CourseComponent } from './component/admin-courses/course.component';
import { TopBarComponent } from './component/top-bar/top-bar.component';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {  MatTableModule } from '@angular/material/table';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatSortModule } from '@angular/material/sort';
import { CourseDetailsComponent } from './component/course-details/course-details.component';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { ProfileComponent } from './component/profile/profile.component';
import { CourseStudentComponent } from './component/course-student/course-student.component';
import { PopUpComponent } from './component/course-pop-up/pop-up.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { ReactiveFormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import {MatIconModule} from '@angular/material/icon';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { AdminStudentsComponent } from './component/admin-students/admin-students.component';
import { StudentInfoComponent } from './component/student-info/student-info.component';
import { StudentDetailsComponent } from './component/student-details/student-details.component';
import { CourseInfoComponent } from './component/course-info/course-info.component';
import { SchedulePopUpComponent } from './component/schedule-pop-up/schedule-pop-up.component';
import { MatSelectModule } from '@angular/material/select';
import { MatDialogModule } from '@angular/material/dialog';
import { ReassignPopUpComponent } from './component/reassign-pop-up/reassign-pop-up.component';
import {MatRadioModule} from '@angular/material/radio';
@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    StudentComponent,
    CourseComponent,
    TopBarComponent,
    CourseDetailsComponent,
    ProfileComponent,
    CourseStudentComponent,
    PopUpComponent,
    AdminStudentsComponent,
    StudentInfoComponent,
    StudentDetailsComponent,
    CourseInfoComponent,
    SchedulePopUpComponent,
    ReassignPopUpComponent,
    ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatInputModule,
    MatIconModule,
    DragDropModule,
    MatSelectModule,
    MatDialogModule,
    MatRadioModule,
  ],
  providers: [
    provideClientHydration(),
    provideHttpClient(withFetch()),
    provideAnimationsAsync()
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }

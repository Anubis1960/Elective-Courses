import { Component, Inject, OnInit } from '@angular/core';
import { Course } from '../../model/course.model';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {MatRadioModule} from '@angular/material/radio';

@Component({
  selector: 'app-reassign-pop-up',
  templateUrl: './reassign-pop-up.component.html',
  styleUrl: './reassign-pop-up.component.css'
})
export class ReassignPopUpComponent implements OnInit{
  input: Course[] | undefined;
  selectedValue: any;
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: {availableCourses: Course[]},
    private ref: MatDialogRef<ReassignPopUpComponent>){}

  ngOnInit(): void {
    this.input = this.data.availableCourses;
    console.log(this.input);
  }

  close() {
    this.ref.close();
  }

  reassign() {
    console.log(this.selectedValue);
    this.ref.close(this.selectedValue);
  }
}

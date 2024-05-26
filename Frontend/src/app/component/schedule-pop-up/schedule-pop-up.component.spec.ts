import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchedulePopUpComponent } from './schedule-pop-up.component';

describe('SchedulePopUpComponent', () => {
  let component: SchedulePopUpComponent;
  let fixture: ComponentFixture<SchedulePopUpComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SchedulePopUpComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(SchedulePopUpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

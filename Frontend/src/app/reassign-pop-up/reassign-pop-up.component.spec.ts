import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReassignPopUpComponent } from './reassign-pop-up.component';

describe('ReassignPopUpComponent', () => {
  let component: ReassignPopUpComponent;
  let fixture: ComponentFixture<ReassignPopUpComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReassignPopUpComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ReassignPopUpComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

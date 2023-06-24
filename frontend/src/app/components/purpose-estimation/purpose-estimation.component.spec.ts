import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PurposeEstimationComponent } from './purpose-estimation.component';

describe('PurposeEstimationComponent', () => {
  let component: PurposeEstimationComponent;
  let fixture: ComponentFixture<PurposeEstimationComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PurposeEstimationComponent]
    });
    fixture = TestBed.createComponent(PurposeEstimationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

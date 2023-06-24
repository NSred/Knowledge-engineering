import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MalfunctionProbabilityComponent } from './malfunction-probability.component';

describe('MalfunctionProbabilityComponent', () => {
  let component: MalfunctionProbabilityComponent;
  let fixture: ComponentFixture<MalfunctionProbabilityComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [MalfunctionProbabilityComponent]
    });
    fixture = TestBed.createComponent(MalfunctionProbabilityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

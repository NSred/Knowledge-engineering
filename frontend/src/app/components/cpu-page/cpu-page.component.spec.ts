import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CpuPageComponent } from './cpu-page.component';

describe('CpuPageComponent', () => {
  let component: CpuPageComponent;
  let fixture: ComponentFixture<CpuPageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CpuPageComponent]
    });
    fixture = TestBed.createComponent(CpuPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

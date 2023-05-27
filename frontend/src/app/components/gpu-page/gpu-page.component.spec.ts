import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GpuPageComponent } from './gpu-page.component';

describe('GpuPageComponent', () => {
  let component: GpuPageComponent;
  let fixture: ComponentFixture<GpuPageComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GpuPageComponent]
    });
    fixture = TestBed.createComponent(GpuPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

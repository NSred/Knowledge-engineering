import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SimilarPcComponent } from './similar-pc.component';

describe('SimilarPcComponent', () => {
  let component: SimilarPcComponent;
  let fixture: ComponentFixture<SimilarPcComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SimilarPcComponent]
    });
    fixture = TestBed.createComponent(SimilarPcComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

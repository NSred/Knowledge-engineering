import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpgradeComponentComponent } from './upgrade-component.component';

describe('UpgradeComponentComponent', () => {
  let component: UpgradeComponentComponent;
  let fixture: ComponentFixture<UpgradeComponentComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [UpgradeComponentComponent]
    });
    fixture = TestBed.createComponent(UpgradeComponentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

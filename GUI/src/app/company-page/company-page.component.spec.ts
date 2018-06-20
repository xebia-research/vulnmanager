import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { companyComponent } from './company-page.component';

describe('companyComponent', () => {
  let component: companyComponent;
  let fixture: ComponentFixture<companyComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ companyComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(companyComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

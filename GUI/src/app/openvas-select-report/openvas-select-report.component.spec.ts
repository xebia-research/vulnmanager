import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenvasSelectReportComponent } from './openvas-select-report.component';

describe('OpenvasSelectReportComponent', () => {
  let component: OpenvasSelectReportComponent;
  let fixture: ComponentFixture<OpenvasSelectReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpenvasSelectReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenvasSelectReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

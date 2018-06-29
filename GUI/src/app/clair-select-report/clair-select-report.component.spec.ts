import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClairSelectReportComponent } from './clair-select-report.component';

describe('ClairSelectReportComponent', () => {
  let component: ClairSelectReportComponent;
  let fixture: ComponentFixture<ClairSelectReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClairSelectReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClairSelectReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

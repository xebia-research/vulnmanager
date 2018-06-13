import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ZapSelectReportComponent } from './zap-select-report.component';

describe('ZapSelectReportComponent', () => {
  let component: ZapSelectReportComponent;
  let fixture: ComponentFixture<ZapSelectReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ZapSelectReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ZapSelectReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

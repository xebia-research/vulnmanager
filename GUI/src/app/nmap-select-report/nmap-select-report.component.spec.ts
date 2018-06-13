import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NmapSelectReportComponent } from './nmap-select-report.component';

describe('NmapSelectReportComponent', () => {
  let component: NmapSelectReportComponent;
  let fixture: ComponentFixture<NmapSelectReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NmapSelectReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NmapSelectReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

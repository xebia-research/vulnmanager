import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NmapResultsComponent } from './nmap-results.component';

describe('NmapResultsComponent', () => {
  let component: NmapResultsComponent;
  let fixture: ComponentFixture<NmapResultsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NmapResultsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NmapResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

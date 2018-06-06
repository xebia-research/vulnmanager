import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ZapResultsComponent } from './zap-results.component';

describe('ZapResultsComponent', () => {
  let component: ZapResultsComponent;
  let fixture: ComponentFixture<ZapResultsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ZapResultsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ZapResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

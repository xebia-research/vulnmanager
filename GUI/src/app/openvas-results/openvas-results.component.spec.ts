import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { OpenvasResultsComponent } from './openvas-results.component';

describe('OpenvasResultsComponent', () => {
  let component: OpenvasResultsComponent;
  let fixture: ComponentFixture<OpenvasResultsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ OpenvasResultsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OpenvasResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

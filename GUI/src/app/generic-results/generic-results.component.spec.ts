import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { GenericResultsComponent } from './generic-results.component';

describe('GenericResultsComponent', () => {
  let component: GenericResultsComponent;
  let fixture: ComponentFixture<GenericResultsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ GenericResultsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(GenericResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ClairResultsComponent } from './clair-results.component';

describe('ClairResultsComponent', () => {
  let component: ClairResultsComponent;
  let fixture: ComponentFixture<ClairResultsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ClairResultsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ClairResultsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

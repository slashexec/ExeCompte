import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ListActivityReportComponent } from './list-activity-report.component';

describe('ListActivityReportComponent', () => {
  let component: ListActivityReportComponent;
  let fixture: ComponentFixture<ListActivityReportComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ ListActivityReportComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ListActivityReportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});

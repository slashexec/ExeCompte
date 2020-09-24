import { TestBed } from '@angular/core/testing';

import { ActivityReportDataService } from './activity-report-data.service';

describe('ActivityReportDataService', () => {
  let service: ActivityReportDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ActivityReportDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

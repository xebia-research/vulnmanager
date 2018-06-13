import { TestBed, inject } from '@angular/core/testing';

import { VulnApiService } from './vuln-api.service';

describe('VulnApiService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [VulnApiService]
    });
  });

  it('should be created', inject([VulnApiService], (service: VulnApiService) => {
    expect(service).toBeTruthy();
  }));
});

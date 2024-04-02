import { TestBed } from '@angular/core/testing';

import { BreweryService } from './brewery.service';

describe('BreweryService', () => {
  let service: BreweryService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BreweryService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});

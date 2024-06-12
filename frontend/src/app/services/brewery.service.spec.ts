import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { BreweryService } from './brewery.service';

describe('BreweryService', () => {
  let service: BreweryService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [BreweryService]
    });

    service = TestBed.inject(BreweryService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should fetch breweries by city', () => {
    const mockBreweries = [{ name: 'Test Brewery' }];

    service.getBreweriesByCity('test').subscribe((breweries: any) => {
      expect(breweries).toEqual(mockBreweries);
    });

    const req = httpMock.expectOne('https://api.openbrewerydb.org/v1/breweries');
    expect(req.request.method).toBe('GET');
    req.flush(mockBreweries);
  });
});

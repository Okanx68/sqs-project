import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { BreweryService } from './services/brewery.service';
import { of } from 'rxjs';
import { ReactiveFormsModule } from '@angular/forms';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let breweryService: BreweryService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule, AppComponent],
      providers: [
        { provide: BreweryService, useValue: { getBreweriesByCity: () => of([]) } }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    breweryService = TestBed.inject(BreweryService);
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should render title', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Brewery Explorer');
  });

  it(`should have the 'Brewery Explorer' title`, () => {
    expect(component.title).toEqual('Brewery Explorer');
  });

  it('should initialize breweries as an empty array', () => {
    expect(component.breweries).toEqual([]);
  });

  it('should initialize noDataMessage correctly', () => {
    expect(component.noDataMessage).toEqual('');
  });

  it('should update breweries and noDataMessage correctly when onSearch is called', () => {
    const mockBreweries = [{ name: 'Test Brewery' }];
    spyOn(breweryService, 'getBreweriesByCity').and.returnValue(of({breweries: JSON.stringify(mockBreweries)}));

    component.onSearch();

    expect(component.breweries.length).toEqual(1);
    expect(component.noDataMessage).toEqual('');
  });

  it('should update breweries to an empty array and noDataMessage to an error message when no data is returned', () => {
    spyOn(breweryService, 'getBreweriesByCity').and.returnValue(of(null));

    component.onSearch();

    expect(component.breweries.length).toEqual(0);
    expect(component.noDataMessage).toEqual('City not found. Try again.');
  });
});

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

  it(`should have the 'Brewery Detail List' title`, () => {
    expect(component.title).toEqual('Brewery Detail List');
  });

  it('should render title', () => {
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Brewery Detail List');
  });

  it(`should have the 'Brewery Details' title`, () => {
    expect(component.title).toEqual('Brewery Details');
  });

  it('should initialize breweries as an empty array', () => {
    expect(component.breweries).toEqual([]);
  });

  it('should initialize noDataMessage correctly', () => {
    expect(component.noDataMessage).toEqual('');
  });

  it('should update breweries and noDataMessage correctly when onSearch is called', () => {
    const mockBreweries = [{ name: 'Test Brewery' }];
    spyOn(breweryService, 'getBreweriesByCity').and.returnValue(of(mockBreweries));

    component.onSearch();

    expect(component.breweries).toEqual(mockBreweries);
    expect(component.noDataMessage).toEqual('');
  });

  it('should update breweries to an empty array and noDataMessage to an error message when no data is returned', () => {
    spyOn(breweryService, 'getBreweriesByCity').and.returnValue(of([]));

    component.onSearch();

    expect(component.breweries).toEqual([]);
    expect(component.noDataMessage).toEqual('');
  });
});

import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {BreweryService} from "./service/brewery.service";
import {Brewery} from "./model/brewery";
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ReactiveFormsModule, NgForOf, NgIf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title: string = 'Brewery Explorer';
  breweries: Brewery[] = [];
  newForm: FormGroup = new FormGroup({
    fieldVal: new FormControl('')
  });
  noDataMessage: string = '';

  constructor(private breweryService: BreweryService) { }

  ngOnInit() {
    this.noDataMessage = "No data fetched, please insert a city name!";
  }

  // Methode zum Abrufen von Brauereidaten aus dem Backend basierend auf dem vom Benutzer eingegebenen Stadtnamen
  onSearch() {
    let enteredCityName = this.newForm.get('fieldVal')?.value;
    this.breweryService.getBreweriesByCity(enteredCityName).subscribe((data: any) => {
          if(data == null){
            this.noDataMessage = "City not found. Try again.";
            this.breweries = [];
          } else {
            this.breweries = JSON.parse(data.breweries);
          }
        }
      );
  }
}

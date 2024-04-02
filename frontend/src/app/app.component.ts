import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {BreweryService} from "./services/brewery.service";
import {brewery} from "./models/brewery";
import {NgForOf} from "@angular/common";

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, ReactiveFormsModule, NgForOf],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'Brewery Details';
  breweries: brewery[] = [];
  newForm = new FormGroup({
    fieldVal: new FormControl('')
  });

  constructor(private breweryService: BreweryService) { }

  ngOnInit() { }

  onSearch() {
    let enteredCityName = this.newForm.get('fieldVal')?.value;
    this.breweryService.getBreweriesByCity(enteredCityName).subscribe((data: any) => {
          console.log(data);
          this.breweries = JSON.parse(data.data);
          console.log(this.breweries);
        }
      );
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { brewery } from '../models/brewery'
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BreweryService {

  constructor(private http: HttpClient) { }

  getBreweriesByCity(cityName: any): any {
    return this.http.get<brewery[]>(environment.apiUrl + '/brewery/' + cityName);
  }
}

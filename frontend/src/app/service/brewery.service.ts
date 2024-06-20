import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Brewery } from '../model/brewery'
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BreweryService {

  constructor(private http: HttpClient) { }

  params = new HttpParams().set('count', '20');

  // erhalte Brauereien vom Backend, die auf den übergebenen Stadtnamen basieren
  getBreweriesByCity(cityName: any): any {
    return this.http.get<Brewery[]>(environment.apiUrl + '/breweries/' + cityName, {params: this.params});
  }
}

import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import { Brewery } from '../models/brewery'
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class BreweryService {

  constructor(private http: HttpClient) { }

  params = new HttpParams().set('count', '20');

  getBreweriesByCity(cityName: any): any {
    return this.http.get<Brewery[]>(environment.apiUrl + '/breweryData/' + cityName, {params: this.params});
  }
}

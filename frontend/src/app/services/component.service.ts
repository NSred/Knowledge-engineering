import { HttpHeaders, HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ComponentService {

  apiHost: string = 'http://localhost:8080/api/';
  headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  getComponent(component : string, motherboard : string, cpu : string, gpu : string, psu : string, ram : string): Observable<any>{
    return this.http.get<any>(this.apiHost + 'component', {params: new HttpParams().set('component', component).set('motherboard', motherboard)
    .set('cpu', cpu).set('gpu', gpu).set('psu', psu).set('ram', ram)});
  }

  getAllCpuNames() : Observable<any>{
    return this.http.get<any>(this.apiHost + 'cpu/names')
  }

  getAllGpuNames() : Observable<any>{
    return this.http.get<any>(this.apiHost + 'gpu/names')
  }

  getAllMotherboardNames() : Observable<any>{
    return this.http.get<any>(this.apiHost + 'motherboard/names')
  }

  getAllPsuNames() : Observable<any>{
    return this.http.get<any>(this.apiHost + 'psu/names')
  }

  getAllRamNames() : Observable<any>{
    return this.http.get<any>(this.apiHost + 'ram/names')
  }
}

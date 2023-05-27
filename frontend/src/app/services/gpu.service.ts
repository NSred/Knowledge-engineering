import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class GpuService {

  apiHost: string = 'http://localhost:8080/api/';
  headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });
  
  constructor(private http: HttpClient) { }

  getGPUBySpec(lowerTdp : number, higherTdp : number, lowerVideoMemory : number, higherVideoMemory : number, lowerCoreClock : number, higherCoreClock : number,
      lowerMemoryClock : number, higherMemoryClock : number): Observable<any>{
    return this.http.get<any>(this.apiHost + 'gpu/gpu-by-spec', {params: new HttpParams().set('lowerTDP', lowerTdp).set('higherTDP', higherTdp)
    .set('lowerVideoMemory', lowerVideoMemory).set('higherVideoMemory', higherVideoMemory).set('lowerCoreClock', lowerCoreClock).set('higherCoreClock', higherCoreClock)
    .set('lowerMemoryClock', lowerMemoryClock).set('higherMemoryClock', higherMemoryClock)});
}
}

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class CpuService {

  apiHost: string = 'http://localhost:8080/api/';
  headers: HttpHeaders = new HttpHeaders({ 'Content-Type': 'application/json' });

  constructor(private http: HttpClient) { }

  getCPUBySpec(lowerTdp : number, higherTdp : number, lowerClockSpeed : number, higherClockSpeed : number, lowerCores : number, higherCores : number,
            lowerThreads : number, higherThreads : number): Observable<any>{
    return this.http.get<any>(this.apiHost + 'cpu/cpu-by-spec', {params: new HttpParams().set('lowerTDP', lowerTdp).set('higherTDP', higherTdp)
    .set('lowerClockSpeed', lowerClockSpeed).set('higherClockSpeed', higherClockSpeed).set('lowerCores', lowerCores).set('higherCores', higherCores)
    .set('lowerThreads', lowerThreads).set('higherThreads', higherThreads)});
  }
}

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

  getPurposeType(cpu_clock_speed_ghz : number, ram_capacity_gb : number, cpu_cores : number, cpu_threads : number, gpu_video_memory_gb : number,
                 gpu_core_clock_mhz : number, hard_drive_capacity_gb : number, psu_power_watts : number, l3_size_mb : number, ram_latency_ns : number) : Observable<any>{
    return this.http.get<any>(this.apiHost + 'component/purpose', 
    {params : new HttpParams().set('cpu_clock_speed_ghz', cpu_clock_speed_ghz)
    .set('ram_capacity_gb', ram_capacity_gb).set('cpu_cores', cpu_cores).set('cpu_threads', cpu_threads).set('gpu_video_memory_gb', gpu_video_memory_gb)
    .set('gpu_core_clock_mhz', gpu_core_clock_mhz).set('hard_drive_capacity_gb', hard_drive_capacity_gb).set('psu_power_watts', psu_power_watts)
    .set('l3_size_mb', l3_size_mb).set('ram_latency_ns', ram_latency_ns)})
  }

  getProbabilityOfCause(allSymptoms : string) : Observable<any>{
    return this.http.get<any>(this.apiHost + 'component/cause', {params : new HttpParams().set('allSymptoms', allSymptoms)})
  }

  getSimilarPCs(cpu : string, gpu : string, motherboard? : string, ram? : string, psu? : string) : Observable<any>{
    return this.http.get<any>(this.apiHost + 'component/similarity', 
    {params: new HttpParams()
      .set('cpu', cpu)
      .set('gpu', gpu)
      .set('motherboard', motherboard || '')
      .set('ram', ram || '')
      .set('psu', psu || '')
      })
  }
}

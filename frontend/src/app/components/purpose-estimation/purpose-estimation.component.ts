import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { PurposeTypeDto } from 'src/app/model/purposeTypeDto.model';
import { ComponentService } from 'src/app/services/component.service';

@Component({
  selector: 'app-purpose-estimation',
  templateUrl: './purpose-estimation.component.html',
  styleUrls: ['./purpose-estimation.component.css']
})
export class PurposeEstimationComponent implements OnInit{

  public cpu_clock_speed_ghz : number = 0;
  public ram_capacity_gb : number = 0;
  public cpu_cores : number = 0;
  public cpu_threads : number = 0;
  public gpu_video_memory_gb : number = 0;
  public gpu_core_clock_mhz : number = 0;
  public hard_drive_capacity_gb : number = 0;
  public psu_power_watts : number = 0;
  public l3_size_mb : number = 0;
  public ram_latency_ns : number = 0;

  public submitted : boolean = false
  public purposeType : PurposeTypeDto = new PurposeTypeDto() 

  constructor(private toast : ToastrService, private service : ComponentService){}

  ngOnInit(): void {
  }

  estimate() : void {
    this.submitted = true
    this.service.getPurposeType(this.cpu_clock_speed_ghz, this.ram_capacity_gb, this.cpu_cores, this.cpu_threads, this.gpu_video_memory_gb, this.gpu_core_clock_mhz,
                                this.hard_drive_capacity_gb, this.psu_power_watts, this.l3_size_mb, this.ram_latency_ns)
        .subscribe({
          next : res => {
            this.toast.success('Success')
            this.purposeType = res
            console.log(this.purposeType);
          },
          error : err => {
            this.toast.error('Error', err)
          }
        })
  }

  getPropertyArray(): { key: string, value: any }[] {
    return Object.entries(this.purposeType).map(([key, value]) => ({ key, value }));
  }
}

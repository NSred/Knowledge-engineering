import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { CpuService } from 'src/app/services/cpu.service';

@Component({
  selector: 'app-cpu-page',
  templateUrl: './cpu-page.component.html',
  styleUrls: ['./cpu-page.component.css']
})
export class CpuPageComponent implements OnInit{

  public lowerTdp : number = 0;
  public higherTdp : number = 0;
  public lowerCores : number = 0;
  public higherCores : number = 0;
  public lowerClockSpeed : number = 0;
  public higherClockSpeed : number = 0;
  public lowerThreads : number = 0;
  public higherThreads : number = 0;

  constructor(private cpuService : CpuService, private toast : ToastrService) {}

  ngOnInit(): void {
  }

  search() : void {
    this.cpuService.getCPUBySpec(this.lowerTdp, this.higherTdp, this.lowerClockSpeed, this.higherClockSpeed, this.lowerCores,
       this.higherCores, this.lowerThreads, this.higherThreads).subscribe( {
        next : res => {
          this.toast.success('Success')
          console.log(res)
        },
        error : err => {
          this.toast.error('Error', err)
        }
       })
  }
}

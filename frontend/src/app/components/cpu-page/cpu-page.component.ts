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

  public cpus : any = []

  public submitted : boolean = false
  constructor(private cpuService : CpuService, private toast : ToastrService) {}

  ngOnInit(): void {
    // this.cpuService.getCPUBySpec(100, 300, 2, 4, 8, 64, 16, 64).subscribe( {
    //    next : res => {
    //      this.temp = res
    //    },
    //    error : err => {
    //    }
    //   })
  }

  search() : void {
    this.submitted = true
    this.cpuService.getCPUBySpec(this.lowerTdp, this.higherTdp, this.lowerClockSpeed, this.higherClockSpeed, this.lowerCores,
       this.higherCores, this.lowerThreads, this.higherThreads).subscribe( {
        next : res => {
          this.toast.success('Success')
          this.cpus = res
          console.log(res)
        },
        error : err => {
          this.toast.error('Error', err)
        }
       })
  }
}

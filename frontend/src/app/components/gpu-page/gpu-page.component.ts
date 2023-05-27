import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { GpuService } from 'src/app/services/gpu.service';

@Component({
  selector: 'app-gpu-page',
  templateUrl: './gpu-page.component.html',
  styleUrls: ['./gpu-page.component.css']
})
export class GpuPageComponent implements OnInit{

  public lowerTdp : number = 0;
  public higherTdp : number = 0;
  public lowerVideoMemory : number = 0;
  public higherVideoMemory : number = 0;
  public lowerCoreClock : number = 0;
  public higherCoreClock : number = 0;
  public lowerMemoryClock : number = 0;
  public higherMemoryClock : number = 0;

  public gpus : any = []
  public submitted : boolean = false

  constructor(private gpuService : GpuService, private toast : ToastrService) {}

  ngOnInit(): void {
  }

  search() : void {
    this.submitted = true
    this.gpuService.getGPUBySpec(this.lowerTdp, this.higherTdp, this.lowerVideoMemory, this.higherVideoMemory, this.lowerCoreClock,
       this.higherCoreClock, this.lowerMemoryClock, this.higherMemoryClock).subscribe( {
        next : res => {
          this.toast.success('Success')
          this.gpus = res
          console.log(res)
        },
        error : err => {
          this.toast.error('Error', err)
        }
       })
  }
}

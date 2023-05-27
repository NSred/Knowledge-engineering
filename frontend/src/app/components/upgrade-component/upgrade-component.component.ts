import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { ComponentService } from 'src/app/services/component.service';

@Component({
  selector: 'app-upgrade-component',
  templateUrl: './upgrade-component.component.html',
  styleUrls: ['./upgrade-component.component.css']
})
export class UpgradeComponentComponent implements OnInit{

  public cpuNames : string[] = []
  public gpuNames : string[] = []
  public motherboardNames : string[] = []
  public psuNames : string[] = []
  public ramNames : string[] = [] 

  public selectedComponent : any
  public selectedCpu : any
  public selectedGpu : any
  public selectedMotherboard : any
  public selectedPsu : any
  public selectedRam : any

  public submitted : boolean = false;

  public result : any 

  constructor(private componentService : ComponentService, private toast : ToastrService) {}

  ngOnInit(): void {
    this.componentService.getAllCpuNames().subscribe( {
      next : res => {
        this.cpuNames = res
      },
      error : err => {
        this.toast.error('Error while getting cpu names')
      }
    })
    this.componentService.getAllGpuNames().subscribe( {
      next : res => {
        this.gpuNames = res
      },
      error : err => {
        this.toast.error('Error while getting gpu names')
      }
    })
    this.componentService.getAllMotherboardNames().subscribe( {
      next : res => {
        this.motherboardNames = res
      },
      error : err => {
        this.toast.error('Error while getting motherboard names')
      }
    })
    this.componentService.getAllPsuNames().subscribe( {
      next : res => {
        this.psuNames = res
      },
      error : err => {
        this.toast.error('Error while getting psu names')
      }
    })
    this.componentService.getAllRamNames().subscribe( {
      next : res => {
        this.ramNames = res
      },
      error : err => {
        this.toast.error('Error while getting ram names')
      }
    })
  }

  search() : void {
    this.submitted = true
    this.componentService.getComponent(this.selectedComponent, this.selectedMotherboard, this.selectedCpu, this.selectedGpu, this.selectedPsu, this.selectedRam)
      .subscribe( {
          next: res => {
            this.toast.success('Success')
            this.result = res
            console.log(this.result)
          },
          error: err => {
            this.toast.error('Error', err)
          }
        })
  }

  changeSubmitted() : void {
    this.submitted = false
  }
}

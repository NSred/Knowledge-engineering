import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { SimilarityEvaluationDto } from 'src/app/model/similarityEvaluationDto.model';
import { ComponentService } from 'src/app/services/component.service';

@Component({
  selector: 'app-similar-pc',
  templateUrl: './similar-pc.component.html',
  styleUrls: ['./similar-pc.component.css']
})
export class SimilarPcComponent implements OnInit{

  public cpuNames : string[] = []
  public gpuNames : string[] = []
  public motherboardNames : string[] = []
  public psuNames : string[] = []
  public ramNames : string[] = [] 

  public selectedCpu : any
  public selectedGpu : any
  public selectedMotherboard : string = ''
  public selectedPsu : string = ''
  public selectedRam : string = ''

  public submitted : boolean = false;

  public similarityList : SimilarityEvaluationDto[] = new Array

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

  findSimilar() : void {
    this.submitted = true
    this.componentService.getSimilarPCs(this.selectedCpu, this.selectedGpu, this.selectedMotherboard, this.selectedRam, this.selectedPsu).subscribe({
      next : res => {
        this.toast.success('Success')
        this.similarityList = res
        console.log(this.similarityList);
      },
      error : err => {
        this.toast.error('Error', err)
      }
    })
  }

  changeSubmitted() : void {
    this.submitted = false
  }
}

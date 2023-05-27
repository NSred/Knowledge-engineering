import { Component, OnInit } from '@angular/core';

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
  constructor() {}

  ngOnInit(): void {
  }
}

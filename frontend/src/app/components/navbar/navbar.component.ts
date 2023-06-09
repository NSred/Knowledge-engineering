import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit{

  constructor(private router : Router) {}

  ngOnInit(): void {
  }

  goToCpusPage() : void {
    this.router.navigateByUrl('cpus')
  }

  goToGpusPage() : void {
    this.router.navigateByUrl('gpus')
  }

  goToHomePage() : void {
    this.router.navigateByUrl('')
  }

  goToUpgradePage() : void {
    this.router.navigateByUrl('upgrade-component')
  }

  goToPurposeEstimationPage() : void {
    this.router.navigateByUrl('purpose-estimation')
  }

  goToMalfunctionProbability() : void {
    this.router.navigateByUrl('malfunction-probability')
  }

  goToSimilarPcPage() : void {
    this.router.navigateByUrl('similar-pc')
  }
}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CpuPageComponent } from './components/cpu-page/cpu-page.component';
import { GpuPageComponent } from './components/gpu-page/gpu-page.component';
import { HomePageComponent } from './components/home-page/home-page.component';
import { MalfunctionProbabilityComponent } from './components/malfunction-probability/malfunction-probability.component';
import { PurposeEstimationComponent } from './components/purpose-estimation/purpose-estimation.component';
import { UpgradeComponentComponent } from './components/upgrade-component/upgrade-component.component';

const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'cpus', component: CpuPageComponent },
  { path: 'gpus', component: GpuPageComponent },
  { path: 'upgrade-component', component: UpgradeComponentComponent },
  { path: 'purpose-estimation', component: PurposeEstimationComponent },
  { path: 'malfunction-probability', component: MalfunctionProbabilityComponent },

];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

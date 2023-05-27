import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CpuPageComponent } from './components/cpu-page/cpu-page.component';
import { GpuPageComponent } from './components/gpu-page/gpu-page.component';
import { HomePageComponent } from './components/home-page/home-page.component';

const routes: Routes = [
  { path: '', component: HomePageComponent },
  { path: 'cpus', component: CpuPageComponent },
  { path: 'gpus', component: GpuPageComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

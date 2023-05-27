import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HomePageComponent } from './home-page/home-page.component';
import { NavbarComponent } from './navbar/navbar.component';
import { CpuPageComponent } from './cpu-page/cpu-page.component';
import { GpuPageComponent } from './gpu-page/gpu-page.component';
import { MaterialModule } from '../material/material/material.module';
import { FormsModule } from '@angular/forms';


@NgModule({
  declarations: [
    HomePageComponent,
    NavbarComponent,
    CpuPageComponent,
    GpuPageComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    MaterialModule,
    FormsModule
  ],
  exports: [
    NavbarComponent,
  ]
})
export class ComponentsModule { }

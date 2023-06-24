import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HomePageComponent } from './home-page/home-page.component';
import { NavbarComponent } from './navbar/navbar.component';
import { CpuPageComponent } from './cpu-page/cpu-page.component';
import { GpuPageComponent } from './gpu-page/gpu-page.component';
import { MaterialModule } from '../material/material/material.module';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { ToastrModule } from 'ngx-toastr';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { UpgradeComponentComponent } from './upgrade-component/upgrade-component.component';
import { PurposeEstimationComponent } from './purpose-estimation/purpose-estimation.component';

@NgModule({
  declarations: [
    HomePageComponent,
    NavbarComponent,
    CpuPageComponent,
    GpuPageComponent,
    UpgradeComponentComponent,
    PurposeEstimationComponent,
  ],
  imports: [
    CommonModule,
    RouterModule,
    MaterialModule,
    FormsModule,
    HttpClientModule,
    ToastrModule.forRoot({
      timeOut: 3000,
      positionClass: 'toast-top-right'
    }),
    BrowserAnimationsModule
  ],
  exports: [
    NavbarComponent,
  ]
})
export class ComponentsModule { }

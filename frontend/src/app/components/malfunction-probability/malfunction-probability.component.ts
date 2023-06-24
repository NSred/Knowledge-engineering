import { Component, OnInit } from '@angular/core';
import { FormControl, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { CauseProbabilityDto } from 'src/app/model/causeProbabilityDto.model';
import { ComponentService } from 'src/app/services/component.service';

@Component({
  selector: 'app-malfunction-probability',
  templateUrl: './malfunction-probability.component.html',
  styleUrls: ['./malfunction-probability.component.css'],
})
export class MalfunctionProbabilityComponent implements OnInit{

  public result : CauseProbabilityDto[] = new Array()
  public options : FormControl = new FormControl('')
  public selectedOptions : any 
  public allSymptoms : string = ''
  public submitted : boolean = false
  public allOptions : string[] = [
    'Overheating', 'Blue_screen_of_death', 'Self_restarting', 'Not_booting', 'Strange_noises', 'Frozen_screen', 'Slow', 'Program_not_responding',
    'No_display', 'Not_starting', 'System_clock_constantly_resetting', 'Laptop_battery_draining', 'Network_issues', 'Low_performance',
    'Virus', 'High_cpu_usage', 'Unresponsive_user_interface', 'Unauthorized_access', 'Application_freezing', 'System_reboots_unexpectedly',
    'Screen_flickering', 'Data_corruption', 'Operating_system_crash', 'Slow_startup', 'Application_crash', 'No_display_on_monitor', 'Speakers_no_sound_output',
    'Headphones_no_sound_output', 'Cursor_movement_issues', 'Keyboard_keys_unresponsive', 'Disc_not_recognized', 'External_optical_drive_tray_not_opening',
    'Webcam_no_video_feed', 'Scanner_not_responding', 'Distorted_scans', 'Printer_not_printing', 'Printer_paper_jams'
  ]

  constructor(private toast : ToastrService, private service : ComponentService){}

  ngOnInit(): void {
  }

  calculate() : void {
    this.allSymptoms = this.options.value.join(',')
    this.submitted = true
    this.service.getProbabilityOfCause(this.allSymptoms).subscribe({
      next : res => {
        this.toast.success('Success')
        this.result = res
        console.log(this.result);
      },
      error : err => {
        this.toast.error('Error', err)
      }
    })
  }
}

import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { nonWhiteSpace } from 'src/app/Constants';
import { EventAddTrigger } from 'src/app/Services/EventAddTrigger';
import { EventService } from 'src/app/Services/EventService';

@Component({
  selector: 'app-add-event',
  templateUrl: './add-event.component.html',
  styleUrls: ['./add-event.component.css']
})
export class AddEventComponent implements OnInit {

  form !: FormGroup

  constructor(private fb: FormBuilder, private eventSvc: EventService, private eventTrigger:EventAddTrigger){}

  ngOnInit(): void {
    this.form = this.createForm();
  }

  createForm(){
    return this.fb.group({
              summary : this.fb.control<string>('', [Validators.required ,nonWhiteSpace, Validators.minLength(2)]),
              description: this.fb.control<string>(''),
              startDate: this.fb.control<string>('', Validators.required),
              endDate: this.fb.control<string>('', Validators.required)
            })
  }

  submitForm(){
    this.eventSvc.addEvent(this.form)
        .then((res:any) => {alert(res['message'])
                            this.form = this.createForm()
                            this.hide()
                          })
        .catch( (err:any) => alert(err.error['message']))
        // must add this trigger into finally clause, if not the trigger will be sent before the http call is completed. This is due to the async nature of the HttpClient function. In finally clause, the trigger will only be performed after the then or catch clause are completed.
        .finally( () => this.eventTrigger.triggerEventRefresh(true) )
  }

  hide(){
    document.getElementById('popup-Form')!.style.display = 'none';
  }

  unhide(){
    document.getElementById('popup-Form')!.style.display = 'block';
  }


}

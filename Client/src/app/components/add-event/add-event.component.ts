import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { nonWhiteSpace } from 'src/app/Constants';
import { EventService } from 'src/app/Services/EventService';

@Component({
  selector: 'app-add-event',
  templateUrl: './add-event.component.html',
  styleUrls: ['./add-event.component.css']
})
export class AddEventComponent implements OnInit {

  form !: FormGroup

  constructor(private fb: FormBuilder, private eventSvc: EventService){}

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
    console.info("Info of this form: ", this.form.value)
    console.info("Specifically the start date: ", this.form.value.startDate)
    console.info("Start date type: ",typeof this.form.value.startDate)
    console.info("Start date getDate: ",this.form.value.startDate.getDate())
    console.info("Start date getMonth: ",this.form.value.startDate.getMonth())
    console.info("Start date getMinutes: ",this.form.value.startDate.getMinutes())

    console.info("Check Start date type: ",typeof this.form.value.startDate ==  typeof Date)
    console.info("Title type: ",typeof this.form.value.summary)

    // this.eventSvc.addEvent(this.form)
    //     .then((res:any) => {alert(res['message'])
    //                         this.form = this.createForm()
    //                       })
    //     .catch( (err:any) => alert(err.error['message']))
  }


}

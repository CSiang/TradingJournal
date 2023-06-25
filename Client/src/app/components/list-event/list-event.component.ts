import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { AddEventComponent } from '../add-event/add-event.component';
import { EventService } from 'src/app/Services/EventService';
import { EventDetail } from 'src/app/Models/EventDetail';
import { Observable, Subscription, timeout } from 'rxjs';
import { EventAddTrigger } from 'src/app/Services/EventAddTrigger';

@Component({
  selector: 'app-list-event',
  templateUrl: './list-event.component.html',
  styleUrls: ['./list-event.component.css']
})
export class ListEventComponent implements OnInit, OnDestroy{

  @ViewChild(AddEventComponent) addEvent !: AddEventComponent; 

  rowContent !: EventDetail
  data !: EventDetail[]
  displayedColumns: string[] = ["summary", "startDate", "endDate"] 
  startDateStr !: string
  endDateStr !: string
  refresh$ !: Subscription

  constructor(private eventSvc: EventService, private eventTrigger: EventAddTrigger){}

  ngOnInit(): void {
    this.refresh$ = this.eventTrigger.trigger$.subscribe(
                            status => {
                              if(status == true){
                                console.info("event trigger is triggered!!!!")
                                this.loadEvents()
                              }
                            } 
                          )
     // for the Date.getMonth() function, it produces number that is current month minus 1 (month-1), hence we will have to perform addition of 1 onto the result to get the accurate month value.
     const nowLong = Date.now()
     const after7DaysLong = nowLong + 7*24*60*60*1000
     const now = new Date(nowLong)
     const after7Days = new Date(after7DaysLong)

     this.startDateStr = `${now.getDate()}/${now.getMonth()+1}/${now.getFullYear()}`
     this.endDateStr = `${after7Days.getDate()}/${after7Days.getMonth()+1}/${after7Days.getFullYear()}`
     this.loadEvents()
  }

  ngOnDestroy(): void {
    this.refresh$.unsubscribe()
  }

  showEventForm(){
    this.addEvent.unhide()
  }
  
  // If 'from' or 'to' are not valid date format, it can't be parsed by new Date(), hence the getDate() method won't produce a valid number and result to a NaN (not a number). Hence we can use the isNaN() method to check if valid number is produced by the input 'from' & 'to' values.
  dateCheck(from: string, to:string){
    return ( isNaN(new Date(from).getDate())|| isNaN(new Date(to).getDate()) || 
            (new Date(from) > new Date(to)))
  }

  // Format of date string to be dd/MM/yyyy. endDate must be later than startDate
  searchEvents(from: string, to:string){
    const fromSplit = from.split("/")
    const toSplit = to.split("/")
    this.startDateStr = fromSplit[1] +'/' + fromSplit[0] +'/' + fromSplit[2]
    this.endDateStr = toSplit[1] +'/' + toSplit[0] +'/' + toSplit[2]
    const startDate = fromSplit[1] +'/' + fromSplit[0] +'/' + fromSplit[2]
    const endDate = toSplit[1] +'/' + toSplit[0] +'/' + toSplit[2]

    this.loadEvents()
  }

  loadEvents(){
    this.eventSvc.getEvent(this.startDateStr, this.endDateStr)
    .then((data:any) => {
                          console.info("data is: ",data)
                          this.data = data
                        })
    .catch((err:any) => console.info("Error is: ", err))
  }

  selectedRow(row:any){
    // from the check, this row object is actually EventDetail object.
    this.rowContent = row;
    // Need to have this setTimeout to read the style property properly..if not it will null....
    setTimeout(() => {
      document.getElementById("popup-1")!.style.display = "block";
    }, 0)
  }

  hideEventDetail(){
    document.getElementById("popup-1")!.style.display = "none";
  }


}

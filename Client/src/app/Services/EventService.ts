import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";
import { EventDetail } from "../Models/EventDetail";

// const BASE_URL = "http://localhost:8080"
const BASE_URL = "https://trading-journal-production.up.railway.app";

@Injectable()
export class EventService{

    constructor(private http: HttpClient){}

    addEvent(form:any){
        const url = BASE_URL + "/calendar/addEvent"

        const data = {
            title: form.value.summary,
            body: form.value.description,
            startDateTime: form.value.startDate,
            endDateTime: form.value.endDate
        }

        return lastValueFrom(this.http.post(url, data))
    }

    getEvent(startDate:string, endDate:string): Promise<EventDetail>{
        const url = BASE_URL + "/calendar/eventList"
        const param = new HttpParams()
                            .set("start", startDate)
                            .set("end", endDate)

        return lastValueFrom(this.http.get<EventDetail>(url, {params: param}))
    }

    deleteEvent(eventId: string){
        const url = BASE_URL + `/calendar/delete/${eventId}`;
        return lastValueFrom(this.http.delete(url));
    }

  

}
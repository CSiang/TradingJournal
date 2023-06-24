import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { lastValueFrom } from "rxjs";

const BASE_URL = "http://localhost:8080"

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

  

}
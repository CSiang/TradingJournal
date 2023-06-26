import { Injectable } from "@angular/core";
import { BehaviorSubject } from "rxjs";

@Injectable()
export class EventAddTrigger{

    private trigger = new BehaviorSubject<boolean>(false)
    trigger$ = this.trigger.asObservable();

    triggerEventRefresh(){
        this.trigger.next(true)
    }

}
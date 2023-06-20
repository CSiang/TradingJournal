import { CollectionViewer, DataSource } from "@angular/cdk/collections";
import { BehaviorSubject, Observable, catchError, finalize, of } from "rxjs";
import { TradingRecord } from "src/app/Models/TradingRecord";
import { HttpService } from "src/app/Services/HttpServices";

export class recordDataSource implements DataSource<TradingRecord> {
    
    private recordSubject = new BehaviorSubject<TradingRecord[]>([]);
    private loadingSubject = new BehaviorSubject<boolean>(false);
    private recordCount = new BehaviorSubject<number>(0);

    public loading$ = this.loadingSubject.asObservable();
    public recordCount$ = this.recordCount.asObservable();

    constructor(private httpSvc: HttpService) {}

    connect(collectionViewer: CollectionViewer): 
    Observable<readonly TradingRecord[]> {
        return this.recordSubject.asObservable();
    }

    disconnect(collectionViewer: CollectionViewer): void {
        this.recordSubject.complete();
        this.loadingSubject.complete();
    }

    getRecordsWithCriteria(holdingStatus: boolean|null, limit: number, offset: number){

        this.loadingSubject.next(true);

        const username = localStorage.getItem('username')
        // @ts-ignore
        this.httpSvc.getTradingRecordsWithCriteria(username, holdingStatus, limit, offset)
        .pipe(
            catchError(() => of([])),
            finalize(() => this.loadingSubject.next(false))
        )
        .subscribe( (data:any) => {
            this.recordSubject.next(data['records'])
            this.recordCount.next(data['count'])
        } )
    }

}
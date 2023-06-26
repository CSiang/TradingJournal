import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { SearchResult } from "../Models/SearchResult";
import { lastValueFrom } from "rxjs";
import { StockGenInfo } from "../Models/StockGenInfo";
import { TradingRecord } from "../Models/TradingRecord";

const BASE_URL = "http://localhost:8080"

@Injectable()
export class HttpService{

    constructor(private http: HttpClient){};

    searchStocks(query: string): Promise<SearchResult[]> {
        let url = BASE_URL + "/api/search";
        const param = new HttpParams().set("query",query )
        return lastValueFrom(this.http.get<SearchResult[]>(url, {params: param}))
    }

    getStockDetails(symbol: string): Promise<StockGenInfo>{
        let url = BASE_URL + "/api/search/" + symbol;
        return lastValueFrom(this.http.get<StockGenInfo>(url))
    }

    saveRecord(tradingRecord: TradingRecord[]){
        const url =  BASE_URL + "/api/saveRecord"

        return lastValueFrom(this.http.post(url, tradingRecord))
    }

    getTradingRecords(username: string){
        const url =  BASE_URL + `/api/allRecords/${username}`

        return lastValueFrom(this.http.get<TradingRecord[]>(url))
    }

    getTradingRecordsWithCriteria(username: string, holdingStatus: boolean|null, limit: number, offset: number){
        const url =  BASE_URL + `/api/records/${username}`

        let param;
        if(holdingStatus != null){
            param = new HttpParams()
                            .set("limit", limit)
                            .set("offset", offset)
                            .set("holdingStatus", holdingStatus)
        } else {
            param = new HttpParams()
            .set("limit", limit)
            .set("offset", offset)
        }
        return this.http.get(url, {params: param})
    }

    deleteRecord(id:number){
        const username = localStorage.getItem("username")
        const url = BASE_URL + `/api/record/${username}/${id}`

        return this.http.delete(url)
    }

    closeTrade(record:TradingRecord){
        const url = BASE_URL + `/api/closeRecord`
        return this.http.put(url, record)
    }

    getPortfolio(){
        const url = BASE_URL + `/api/tradeSummary`
        return lastValueFrom(this.http.get(url))
    }

}
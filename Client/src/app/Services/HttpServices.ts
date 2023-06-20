import { HttpClient, HttpParams } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { SearchResult } from "../Models/SearchResut";
import { lastValueFrom } from "rxjs";
import { StockGenInfo } from "../Models/StockGenInfo";
import { TradingRecord } from "../Models/TradingRecord";

const BASE_URL = "http://localhost:8080/api"

@Injectable()
export class HttpService{

    constructor(private http: HttpClient){};

    searchStocks(query: string): Promise<SearchResult[]> {
        // let url = BASE_URL + "/search/" +  query;
        let url = BASE_URL + "/search";
        const param = new HttpParams().set("query",query )
        return lastValueFrom(this.http.get<SearchResult[]>(url, {params: param}))
    }

    getStockDetails(symbol: string): Promise<StockGenInfo>{
        let url = BASE_URL + "/search/" + symbol;
        return lastValueFrom(this.http.get<StockGenInfo>(url))
    }

    saveRecord(tradingRecord: TradingRecord[]){
        const url =  BASE_URL + "/saveRecord"
        // JSON.stringify(tradingRecord)
        // return lastValueFrom(this.http.post(url, JSON.stringify(tradingRecord)))
        return lastValueFrom(this.http.post(url, tradingRecord))
    }

    getTradingRecords(username: string){
        const url =  BASE_URL + `/allRecords/${username}`

        return lastValueFrom(this.http.get<TradingRecord[]>(url))
    }

    getTradingRecordsWithCriteria(username: string, holdingStatus: boolean|null, limit: number, offset: number){
        const url =  BASE_URL + `/records/${username}`

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
        const url = BASE_URL + `/record/${username}/${id}`

        return this.http.delete(url)
    }

    closeTrade(record:TradingRecord){
        const url = BASE_URL + `/closeRecord`
        return this.http.put(url, record)
    }

    getPortfolio(){
        const url = BASE_URL + `/tradeSummary`
        return lastValueFrom(this.http.get(url))
    }

    // getStockPrice(tickerList: string[]){
    //     const firstTicker = tickerList.shift()
    //     const url = `https://query2.finance.yahoo.com/v8/finance/chart/${firstTicker}`
    //     const param = new HttpParams().set("range", "1d").set("interval", "1d")
    //                                   .set("comparisons", tickerList.toString())
    //     console.info("firstTicker: ", firstTicker)
    //     console.info("tickerList to string: ", tickerList.toString())

    //     return lastValueFrom(this.http.get(url, {params: param}))
    // }

    // getStockPrice(){
    //     const url = "https://query2.finance.yahoo.com/v8/finance/chart/AAPL?range=1d&interval=1d&comparisons=META,TSLA,BABA,AMZN,BS6.SI"

    //     return lastValueFrom(this.http.get(url))
    // }

}
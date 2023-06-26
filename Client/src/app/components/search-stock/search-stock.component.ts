import { Component } from '@angular/core';
import { SearchResult } from 'src/app/Models/SearchResult';
import { HttpService } from 'src/app/Services/HttpServices';

@Component({
  selector: 'app-search-stock',
  templateUrl: './search-stock.component.html',
  styleUrls: ['./search-stock.component.css']
})
export class SearchStockComponent {

  stockList : SearchResult[] = []
  message: string = ""

  constructor(private httpSvc: HttpService){}

  searchStocks(query: string){
    
    this.httpSvc.searchStocks(query)
        .then(data => {this.stockList = data
                       this.message = ""} )
        .catch(error => {
                console.error("error: ", error)
                console.error("Status: ", error.status)
              if(error.status == 0){
                this.message = "No response from server."
              } else {
                this.message = "Not Stock found"
                this.stockList.length = 0;
              }
            } 
          );


  }
}

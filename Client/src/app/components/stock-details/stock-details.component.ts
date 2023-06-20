import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { StockGenInfo } from 'src/app/Models/StockGenInfo';
import { HttpService } from 'src/app/Services/HttpServices';

@Component({
  selector: 'app-stock-details',
  templateUrl: './stock-details.component.html',
  styleUrls: ['./stock-details.component.css']
})
export class StockDetailsComponent implements OnInit {

  stockSymbol !: string
  GenInfo !: StockGenInfo
  message !: string

  constructor(private actRoute: ActivatedRoute, private httpSvc: HttpService,
              private router: Router){}

  ngOnInit(): void {

    this.stockSymbol = this.actRoute.snapshot.params['symbol'];
    console.info('Stock symbol: ', this.stockSymbol)

    this.httpSvc.getStockDetails(this.stockSymbol)
        .then(data => {this.GenInfo = data
                        console.info("Stock Gen Info: ", this.GenInfo)
                      })
        .catch(err => {
          if(err.status == 500){
            this.message = "Internal server error...please contact administrator."
          }
        })
  }

  addRecord(){
    this.router.navigate(['/records', {ticker:this.GenInfo.symbol, stockName: this.GenInfo.longName }])
  }



}

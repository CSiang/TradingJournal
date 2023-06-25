import { Component, OnInit, ViewChild } from '@angular/core';
import { RecordSummary } from 'src/app/Models/RecordSummary';
import { SummaryTotal } from 'src/app/Models/SummaryTotal';
import { HttpService } from 'src/app/Services/HttpServices';
import { AddEventComponent } from '../add-event/add-event.component';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{

  soldSummary !: RecordSummary[];
  holdSummary !: RecordSummary[];
  holdTotal : SummaryTotal[] = []
  soldTotal : SummaryTotal[] = []
  displayList : SummaryTotal[][] = [this.holdTotal, this.soldTotal]
  summaryList : RecordSummary[][] = [this.holdSummary, this.soldSummary]
  toggleButton = "Expand"
  // tickerList : string[] = ["AAPL", "META", "AMZN"]

  constructor(private httpSvc:HttpService){}

  // Need to take note of the currency difference of the ticker (some usd some sgd, can't just add them up together.)
  ngOnInit(): void {
    // this.httpSvc.getStockPrice()
    //             .then(data => console.info("Data from getStockPrice: ", data))

    this.httpSvc.getPortfolio()
        .then((data:any) => {
                            this.holdSummary = data['hold']
                            console.info("holdSummary: ", this.holdSummary)
                            this.soldSummary = data['sold']
                            console.info("soldSummary: ", this.soldSummary)
                      })
        .finally(() => {
              // to find the total of hold
              for(let i=0; i<this.holdSummary.length; i++){
                let total = this.holdTotal.find(v => v.currency === this.holdSummary[i].currency)
                if( total != undefined){
                  total.amount += (+this.holdSummary[i].totalWorth)
                } else {
                  let newTotal = {currency: this.holdSummary[i].currency,
                                  amount: (+this.holdSummary[i].totalWorth),
                                  holding: true}
                  this.holdTotal.push(newTotal)
                }
                this.holdSummary[i].stockReturn = this.holdSummary[i].totalUnit 
                                                 * (+this.holdSummary[i].stockPrice)
              }

              // to find the total of sold
              for(let i=0; i<this.soldSummary.length; i++){
                let total = this.soldTotal.find(v => v.currency === this.soldSummary[i].currency)
                if( total != undefined){
                  total.amount += (+this.soldSummary[i].stockReturn)
                } else {
                  let newTotal = {currency: this.soldSummary[i].currency,
                                  amount: (+this.soldSummary[i].stockReturn),
                                  holding: false}
                  this.soldTotal.push(newTotal)
                }

              }
            })
  }


  // h2MoustOver(){
  //   document.getElementById("title")!.style.color = "violet"
  //   document.getElementById("title")!.style.fontWeight = "bold"
  // }

  // h2MoustLeave(){
  //   document.getElementById("title")!.style.color = "black"
  //   document.getElementById("title")!.style.fontWeight = "normal"
  // }

  tableDisplay(idx: number){
    let tableNum = `table-${idx}`
    let buttonLess =  `button-less-${idx}`
    let buttonMore =  `button-more-${idx}`
    let displayStyle = document.getElementById(tableNum)?.style.display
    console.info("tableNum, buttonLess, buttonMore: ", tableNum,buttonLess, buttonMore)

    if(displayStyle === "none"){
      document.getElementById(tableNum)!.style.display = "contents"
      document.getElementById(buttonLess)!.style.display = "none"
      document.getElementById(buttonMore)!.style.display = "contents"
    } else {
      document.getElementById(tableNum)!.style.display = "none"
      document.getElementById(buttonLess)!.style.display = "contents"
      document.getElementById(buttonMore)!.style.display = "none"
    }
  }

  expandAll(){
    if(this.toggleButton === "Expand"){
      document.getElementsByName("summaryTable").forEach(
        data => {data.style.display = "contents"}
      )
      document.getElementsByName("iconLess").forEach(
        data => {data.style.display = "none"}
      )
      document.getElementsByName("iconMore").forEach(
        data => {data.style.display = "contents"}
      )
      this.toggleButton = "Collapse"

    } else if (this.toggleButton === "Collapse"){
      document.getElementsByName("summaryTable").forEach(
        data => {data.style.display = "none"}
      )
      document.getElementsByName("iconLess").forEach(
        data => {data.style.display = "contents"}
      )
      document.getElementsByName("iconMore").forEach(
        data => {data.style.display = "none"}
      )
      this.toggleButton = "Expand"
    }
  }

}

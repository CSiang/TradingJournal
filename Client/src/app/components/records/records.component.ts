import { AfterViewInit, Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, lastValueFrom } from 'rxjs';
import { nonWhiteSpace, orderDate } from 'src/app/Constants';
import { TradingRecord } from 'src/app/Models/TradingRecord';
import { HttpService } from 'src/app/Services/HttpServices';
import { recordDataSource } from './records.datasource';
import { MatPaginator } from '@angular/material/paginator';

@Component({
  selector: 'app-records',
  templateUrl: './records.component.html',
  styleUrls: ['./records.component.css']
})
export class RecordsComponent implements OnInit, AfterViewInit, OnDestroy {

  @ViewChild(MatPaginator) paginator !: MatPaginator

  // holdingStatus: boolean|null = true
  holdingStatus: boolean|null = null
  recordLimit:number = 5
  recordOffset: number = 0
  recordCountObs !: Observable<number>
  pageSizeOption :number[] = [1,2,5,10]
  deleteToggle : boolean = false
  delRecord !: TradingRecord

  form !: FormGroup
  sellForm !: FormGroup
  array !: FormArray
  // displayedColumns: string[] = ['stockTicker', 'stockName', 'currency', 'buyPrice', 'buyAmount','buyDate', 'actions'];
  displayedColumns: string[] = ['stockTicker', 'stockName', 'currency', 'buyPrice','sellPrice', 'buyAmount','buyDate', 'sellDate','actions'];
  dataSource !: recordDataSource
  sellRecord !: TradingRecord

  constructor(private fb: FormBuilder, private httpSvc: HttpService, 
              private router: Router, private actRoute: ActivatedRoute){}
  
  ngOnInit(): void {
    const stockTicker = this.actRoute.snapshot.params["ticker"]
    const stockName = this.actRoute.snapshot.params["stockName"]

    this.form = this.createForm()
    this.dataSource = new recordDataSource(this.httpSvc)
    this.dataSource.getRecordsWithCriteria(this.holdingStatus, this.recordLimit, this.recordOffset)
    this.recordCountObs = this.dataSource.recordCount$

    if(stockTicker || stockName){
      this.addRecord(stockTicker, stockName)
    }
  }
  
  ngAfterViewInit(): void {
    this.paginator.page.subscribe(
      () => {
        this.recordLimit = this.paginator.pageSize;
        this.recordOffset = this.paginator.pageIndex * this.recordLimit;
        this.loadPage();
      }
      )
    }

  ngOnDestroy(): void {
      
  }
    
  createForm(){
    this.array = this.fb.array([])
    return this.fb.group({
      record: this.array
    })
  }

  addRecord(tickerIn?: string, nameIn?: string){
    let sTicker="", sName =""
    if(tickerIn && nameIn){
      sTicker = tickerIn
      sName = nameIn
    }
    const record = this.fb.group({
        tradingAccount: this.fb.control<string>('',[Validators.required, nonWhiteSpace]),
        stockTicker: this.fb.control<string>(sTicker,[Validators.required, nonWhiteSpace]),
        stockName: this.fb.control<string>(sName,[Validators.required, nonWhiteSpace]),
        currency: this.fb.control<string>('',[Validators.required, nonWhiteSpace]),
        buyPrice: this.fb.control<number>(0,[Validators.required, Validators.min(0.0001)]),
        buyAmount: this.fb.control<number>(0,[Validators.required, Validators.min(1)]),
        buyDate: this.fb.control<Date>(new Date(), [Validators.required, orderDate]),
        buyTransactionCost: this.fb.control<number>(0),
        sellTransactionCost: this.fb.control<number>(0)
    })
    this.array.push(record)
  }

  delTask(idx: number){
    this.array.removeAt(idx)
  }

  closeAddForm(){
    this.array.clear()
  }

  saveRecords(){
    console.info("Form value: ", this.form.value)
    console.info("Array: ", this.array)
    const savedUsername = localStorage.getItem("username")
    
    // both testRecords and records works, Server able to receive the data.
    // The difference is for testRecords, those fields not set by form will b as null.
    // const testRecords: TradingRecord[] = this.array.value.map((item:any) => item as TradingRecord)    
    const records: TradingRecord[] = this.array.value.map((item:any) => {return {
                  id: '',
                  username: savedUsername,
                  tradingAccount: item['tradingAccount'],
                  stockTicker: item['stockTicker'],
                  stockName: item['stockName'],
                  currency: item['currency'],
                  buyPrice: item['buyPrice'],
                  sellPrice: 0,
                  buyAmount: item['buyAmount'],
                  holdingStatus: true,
                  buyDate: item['buyDate'],
                  sellDate: null,
                  buyTransactionCost: item['buyTransactionCost'],
                  sellTransactionCost: item['sellTransactionCost']
                  } as unknown as TradingRecord
                } )
  
    console.info("Record: ",records)

    this.httpSvc.saveRecord(records)
      .then((data:any) => {
        alert(data['message'])
        this.loadPage()
      })
      .catch(err => {console.info("error: ", err)})
  }

  formValid(){
    return this.form.invalid 
  }

  loadPage(){
    this.dataSource.getRecordsWithCriteria(this.holdingStatus, this.recordLimit, this.recordOffset)
    this.array.clear()
  }

  changeHoldingStatus(value: string){
    switch(value) { 
      case "true": { 
        this.holdingStatus = true;
        break; 
      } 
      case "false": { 
        this.holdingStatus = false;
        break; 
      } 
      case "all": { 
        this.holdingStatus = null;
        break; 
     } 
      default: { 
        break; 
      } 
   } 

   this.resetPage()
   this.loadPage()
  }
  
  resetPage(){
    this.recordLimit = 5
    this.recordOffset = 0
    this.paginator.pageIndex = 0
  }

  deletePrompt(record: any){
        this.delRecord = record;
        console.info("record: ", this.delRecord)
        setTimeout(() => {
          document.getElementById("delConfirm")!.style.borderColor = "red"
          document.getElementById("delConfirm")!.style.display = "block";
        }, 0)
  }

  deleteRecord(){
    const id: number = +this.delRecord.id
    lastValueFrom(this.httpSvc.deleteRecord(id))
                  .then((data:any) => {console.info("Response from server: ", data)
                                      alert(data['message'])
                                      document.getElementById("delConfirm")!.style.display = "none";
                                }
                        )
                  .catch(err => console.info("Error: ", err))
                  .finally(() => this.loadPage())
  }

  cancelDelete(){
    document.getElementById("delConfirm")!.style.borderColor = "black"
    document.getElementById("delConfirm")!.style.display = "none";
  }

  delToggle(event: any){
    this.deleteToggle = event['checked']
  }

  updateRecord(record:TradingRecord){
    this.createSellForm()
    this.sellRecord = record;
    console.info("sellRecord: ", this.sellRecord)
    // Need to use this setTimeout to ensure that the sellRecord is assigned before accessing to DOM element, if not will have error and popup form won't display correctly at the 1st time.
    setTimeout(() => {
      document.getElementById("myForm")!.style.display = 'block'
    }, 0);
  }

  createSellForm(){
    this.sellForm = this.fb.group({
                        sellPrice: this.fb.control<number>(0,[Validators.required, Validators.min(0.0001)]),
                        sellDate: this.fb.control<Date>(new Date(), [Validators.required, orderDate]),
                        sellTransactionCost: this.fb.control<number>(0)
                      })
  }

  closeForm(){
    document.getElementById("myForm")!.style.display = 'none'
  }

  closeTrade(){
    console.info("Value of the record to be updated: ", this.sellRecord)
    console.info("Value of sell form: ",this.sellForm.value)
    this.sellRecord['sellDate'] = this.sellForm.value['sellDate']
    this.sellRecord['sellPrice'] = this.sellForm.value['sellPrice']
    this.sellRecord['sellTransactionCost'] = this.sellForm.value['sellTransactionCost']
    console.info("Value of the record after updating with form: ", this.sellRecord)

    this.httpSvc.closeTrade(this.sellRecord).subscribe({
      next: (data:any) => alert(data['message']),
      error: (err:any) => alert(err['message']),
      complete: () => document.getElementById("myForm")!.style.display = 'none'
    })

    // setTimeout(() => {
    //   document.getElementById("myForm")!.style.display = 'none'
    // }, 0);
  }
}



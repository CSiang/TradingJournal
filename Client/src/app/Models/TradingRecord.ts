export interface TradingRecord{
    id: number,
    username: string,
    tradingAccount: string,
    stockTicker: string,
    stockName: string,
    currency: string,
    buyPrice: number,
    sellPrice: number,
    buyAmount: number
    holdingStatus: boolean,
    buyDate: Date,
    sellDate: Date,
    buyTransactionCost: number,
    sellTransactionCost: number
}
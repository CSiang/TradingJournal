import { StockFinOverview } from "./StockFinOverview";

export interface StockGenInfo{
    symbol: string,
    longName: string,
    timeZone: string,
    address1: string,
    address2: string,
    city: string,
    zip: string,
    country: string,
    phone: string,
    website: string,
    industry: string,
    sector: string,
    businessSummary: string,
    finOverview: StockFinOverview
}



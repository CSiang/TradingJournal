package tfip_project.financial_analysis.Models;

public class FinancialData {
    
    // Currency is from "financialData" -> "financialCurrency"
    private String currency;

    // Below all in "balanceSheetStatements" jsonObject
    private int revenue; // Total revenue
    private int profitAfterTax; // Profit after taxation ("netIncome")
    private int cashflowFromOpr; // Cash Flow from Operating Activities
    private int totalEquities; // totalStockholderEquity
    private int retainedEarnings;  //retainedEarnings
    private int cash; // cash
    private int receivable; // netReceivables
    private int totalLia; // totalnonCurrentLiability =  totalLiab - totalCurrentLiabilities
    private int currentLia; // totalCurrentLiabilities





}

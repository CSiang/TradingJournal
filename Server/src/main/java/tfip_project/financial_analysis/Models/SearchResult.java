package tfip_project.financial_analysis.Models;

public class SearchResult {
    
    private String symbol;
    private String name;
    private String type;
    private String stockExchange;

    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getStockExchange() {
        return stockExchange;
    }
    public void setStockExchange(String stockExchange) {
        this.stockExchange = stockExchange;
    }

    @Override
    public String toString() {
        return "SearchResult [symbol=" + symbol + ", name=" + name + ", type=" + type + ", stockExchange="
                + stockExchange + "]";
    }
}

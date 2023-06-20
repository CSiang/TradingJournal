package tfip_project.financial_analysis.Models;

import java.sql.Date;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "trading_record")
public class TradingRecord {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private String username;
    private String tradingAccount;
    private String stockTicker;
    private String stockName;
    private String currency;
    private Float buyPrice;
    private Float sellPrice;
    private int buyAmount;
    private boolean holdingStatus;
    private Date buyDate;
    private Date sellDate;
    private Float buyTransactionCost;
    private Float sellTransactionCost;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getTradingAccount() {
        return tradingAccount;
    }
    public void setTradingAccount(String tradingAccount) {
        this.tradingAccount = tradingAccount;
    }
    public String getStockTicker() {
        return stockTicker;
    }
    public void setStockTicker(String stockTicker) {
        this.stockTicker = stockTicker;
    }
    public String getStockName() {
        return stockName;
    }
    public void setStockName(String stockName) {
        this.stockName = stockName;
    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    public Float getBuyPrice() {
        return buyPrice;
    }
    public void setBuyPrice(Float buyPrice) {
        this.buyPrice = buyPrice;
    }
    public Float getSellPrice() {
        return sellPrice;
    }
    public void setSellPrice(Float sellPrice) {
        this.sellPrice = sellPrice;
    }
    public int getBuyAmount() {
        return buyAmount;
    }
    public void setBuyAmount(int buyAmount) {
        this.buyAmount = buyAmount;
    }
    public boolean isHoldingStatus() {
        return holdingStatus;
    }
    public void setHoldingStatus(boolean holdingStatus) {
        this.holdingStatus = holdingStatus;
    }
    public Date getBuyDate() {
        return buyDate;
    }
    public void setBuyDate(Date buyDate) {
        this.buyDate = buyDate;
    }
    public Date getSellDate() {
        return sellDate;
    }
    public void setSellDate(Date sellDate) {
        this.sellDate = sellDate;
    }
    public Float getBuyTransactionCost() {
        return buyTransactionCost;
    }
    public void setBuyTransactionCost(Float buyTransactionCost) {
        this.buyTransactionCost = buyTransactionCost;
    }
    public Float getSellTransactionCost() {
        return sellTransactionCost;
    }
    public void setSellTransactionCost(Float sellTransactionCost) {
        this.sellTransactionCost = sellTransactionCost;
    }



    // public JsonObject toJson(TradingRecord record){
    //     JsonObjectBuilder job = Json.createObjectBuilder();
    //     job.add("id", record.getId())
    //         .add("tradingAccount", record.getTradingAccount())
    //         .add("stockTicker", record.getStockTicker())
    //         .add("stockName", record.getStockName())
    //         .add("currency", record.getCurrency())
    //         .add("buyPrice", record.getBuyPrice())
    //         .add("sellPrice", record.getSellPrice())
    //         .add("buyAmount", record.getBuyAmount())
    //         .add("holdingStatus", record.isHoldingStatus())
    //         .add("buyDate", record.getBuyDate().toString())
    //         .add("sellDate", record.getSellDate().toString())
    //         .add("buyTransactionCost", record.getBuyTransactionCost())
    //         .add("sellTransactionCost", record.getSellTransactionCost());

    //     return job.build();
    // }

    @Override
    public String toString() {
        return "TradingRecord [id=" + id + ", userId=" + userId + ", username=" + username + ", tradingAccount="
                + tradingAccount + ", stockTicker=" + stockTicker + ", stockName=" + stockName + ", currency="
                + currency + ", buyPrice=" + buyPrice + ", sellPrice=" + sellPrice + ", buyAmount=" + buyAmount
                + ", holdingStatus=" + holdingStatus + ", buyDate=" + buyDate + ", sellDate=" + sellDate
                + ", buyTransactionCost=" + buyTransactionCost + ", sellTransactionCost=" + sellTransactionCost + "]";
    }

    public JsonObject toJson(){
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", getId())
            .add("userId", getUserId())
            .add("username", getUsername())
            .add("tradingAccount", getTradingAccount())
            .add("stockTicker", getStockTicker())
            .add("stockName", getStockName())
            .add("currency", getCurrency())
            .add("buyPrice", getBuyPrice().toString())
            .add("sellPrice", getSellPrice().toString())
            .add("buyAmount", getBuyAmount())
            .add("holdingStatus", isHoldingStatus())
            .add("buyDate", getBuyDate().toString())
            .add("sellDate", getSellDate() != null? getSellDate().toString(): "")
            .add("buyTransactionCost", getBuyTransactionCost().toString())
            .add("sellTransactionCost", getSellTransactionCost() != null ? getSellTransactionCost().toString():"");

        return job.build();
    }
      
    
    
}


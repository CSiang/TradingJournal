package tfip_project.financial_analysis.Models;

import java.util.Date;

public class StockFinOverview {
    
    private float trailingPE;
    private float dividend;
    private Date exDividendDate;
    private float aveDivYield5Years;
    private float payoutRatio;
    private float beta;
    private int marketCap;
    private float fifty2WeekHigh;
    private float fifty2WeekLow;
    private int averageDailyVol10Days;
    private int averageVol3Months;

    public float getTrailingPE() {
        return trailingPE;
    }
    public void setTrailingPE(float trailingPE) {
        this.trailingPE = trailingPE;
    }
    public float getDividend() {
        return dividend;
    }
    public void setDividend(float dividend) {
        this.dividend = dividend;
    }
    public Date getExDividendDate() {
        return exDividendDate;
    }
    public void setExDividendDate(Date exDividendDate) {
        this.exDividendDate = exDividendDate;
    }
    public float getAveDivYield5Years() {
        return aveDivYield5Years;
    }
    public void setAveDivYield5Years(float aveDivYield5Years) {
        this.aveDivYield5Years = aveDivYield5Years;
    }
    public float getPayoutRatio() {
        return payoutRatio;
    }
    public void setPayoutRatio(float payoutRatio) {
        this.payoutRatio = payoutRatio;
    }
    public float getBeta() {
        return beta;
    }
    public void setBeta(float beta) {
        this.beta = beta;
    }
    public int getMarketCap() {
        return marketCap;
    }
    public void setMarketCap(int marketCap) {
        this.marketCap = marketCap;
    }
    public float getFifty2WeekHigh() {
        return fifty2WeekHigh;
    }
    public void setFifty2WeekHigh(float fifty2WeekHigh) {
        this.fifty2WeekHigh = fifty2WeekHigh;
    }
    public float getFifty2WeekLow() {
        return fifty2WeekLow;
    }
    public void setFifty2WeekLow(float fifty2WeekLow) {
        this.fifty2WeekLow = fifty2WeekLow;
    }
    public int getAverageDailyVol10Days() {
        return averageDailyVol10Days;
    }
    public void setAverageDailyVol10Days(int averageDailyVol10Days) {
        this.averageDailyVol10Days = averageDailyVol10Days;
    }
    public int getAverageVol3Months() {
        return averageVol3Months;
    }
    public void setAverageVol3Months(int averageVol3Months) {
        this.averageVol3Months = averageVol3Months;
    }
    @Override
    public String toString() {
        return "StockFinOverview [trailingPE=" + trailingPE + ", dividend=" + dividend + ", exDividendDate="
                + exDividendDate + ", aveDivYield5Years=" + aveDivYield5Years + ", payoutRatio=" + payoutRatio
                + ", beta=" + beta + ", marketCap=" + marketCap + ", fifty2WeekHigh=" + fifty2WeekHigh
                + ", fifty2WeekLow=" + fifty2WeekLow + ", averageDailyVol10Days=" + averageDailyVol10Days
                + ", averageVol3Months=" + averageVol3Months + "]";
    }

}

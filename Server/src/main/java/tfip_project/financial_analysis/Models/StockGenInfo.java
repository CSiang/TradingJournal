package tfip_project.financial_analysis.Models;

public class StockGenInfo {
    
    private String symbol;
    private String longName;
    private String timeZone;
    private String address1;
    private String address2;
    private String city;
    private String zip;
    private String country;
    private String phone;
    private String website;
    private String industry;
    private String sector;
    private String businessSummary;
    private StockFinOverview finOverview;
    
    public String getSymbol() {
        return symbol;
    }
    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
    public String getLongName() {
        return longName;
    }
    public void setLongName(String longName) {
        this.longName = longName;
    }
    public String getTimeZone() {
        return timeZone;
    }
    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
    public String getAddress1() {
        return address1;
    }
    public void setAddress1(String address1) {
        this.address1 = address1;
    }
    public String getAddress2() {
        return address2;
    }
    public void setAddress2(String address2) {
        this.address2 = address2;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getCountry() {
        return country;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getWebsite() {
        return website;
    }
    public void setWebsite(String website) {
        this.website = website;
    }
    public String getIndustry() {
        return industry;
    }
    public void setIndustry(String industry) {
        this.industry = industry;
    }
    public String getSector() {
        return sector;
    }
    public void setSector(String sector) {
        this.sector = sector;
    }
    public String getBusinessSummary() {
        return businessSummary;
    }
    public void setBusinessSummary(String businessSummary) {
        this.businessSummary = businessSummary;
    }
    public StockFinOverview getFinOverview() {
        return finOverview;
    }
    public void setFinOverview(StockFinOverview finOverview) {
        this.finOverview = finOverview;
    }

    
    @Override
    public String toString() {
        return "StockGenInfo [symbol=" + symbol + ", longName=" + longName + ", timeZone=" + timeZone + ", address1="
                + address1 + ", address2=" + address2 + ", city=" + city + ", zip=" + zip + ", country=" + country
                + ", phone=" + phone + ", website=" + website + ", industry=" + industry + ", sector=" + sector
                + ", businessSummary=" + businessSummary + ", finOverview=" + finOverview + "]";
    }
    
}

package tfip_project.financial_analysis;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Set;

import org.springframework.stereotype.Component;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import tfip_project.financial_analysis.Models.SearchResult;
import tfip_project.financial_analysis.Models.StockFinOverview;
import tfip_project.financial_analysis.Models.StockGenInfo;

@Component
public class Util {

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public SearchResult getSearchResult(JsonObject jo){
        
        SearchResult sr = new SearchResult();

        sr.setSymbol(jo.getString("symbol"));
        sr.setName(jo.getString("name"));
        sr.setType(jo.getString("typeDisp"));
        sr.setStockExchange(jo.getString("exchDisp"));
        
        return sr;
    }

    public StockGenInfo jsonToGenInfo(String payload){
        JsonReader jr = Json.createReader(new StringReader(payload));
        JsonObject jo = jr.readObject().getJsonObject("quoteSummary")
                          .getJsonArray("result").getJsonObject(0);

        JsonObject quoteType = jo.getJsonObject("quoteType");
        JsonObject summaryProfile = jo.getJsonObject("summaryProfile");

        StockGenInfo genInfo = new StockGenInfo();
        genInfo.setSymbol(quoteType.getString("symbol"));
        genInfo.setLongName(quoteType.getString("longName"));
        genInfo.setTimeZone(quoteType.getString("timeZoneFullName"));

        if(null != summaryProfile){
        if(summaryProfile.containsKey("address1")){
            genInfo.setAddress1(summaryProfile.getString("address1"));
        }
        if(summaryProfile.containsKey("address2")){
            genInfo.setAddress2(summaryProfile.getString("address2"));
        }
        if(summaryProfile.containsKey("city") && summaryProfile.containsKey("state")){
            genInfo.setCity(
            summaryProfile.getString("city")+ " " + summaryProfile.getString("state") );
        } else{
            genInfo.setCity(summaryProfile.getString("city"));
        }
        if(summaryProfile.containsKey("zip")){
            genInfo.setZip(summaryProfile.getString("zip"));
        }
        if(summaryProfile.containsKey("country")){
            genInfo.setCountry(summaryProfile.getString("country"));
        }
        if(summaryProfile.containsKey("phone")){
            genInfo.setPhone(summaryProfile.getString("phone"));
        }
        if(summaryProfile.containsKey("website")){
            genInfo.setWebsite(summaryProfile.getString("website"));
        }
        if(summaryProfile.containsKey("industry")){
            genInfo.setIndustry(summaryProfile.getString("industry"));
        }
        if(summaryProfile.containsKey("sector")){
            genInfo.setSector(summaryProfile.getString("sector"));
        }
        if(summaryProfile.containsKey("longBusinessSummary")){
            genInfo.setBusinessSummary(summaryProfile.getString("longBusinessSummary"));
        }
        }
        
        return genInfo;
    }

    public StockFinOverview jsonToStockFinOverview(String payload) throws ParseException{

        JsonReader jr = Json.createReader(new StringReader(payload));
        JsonObject jo = jr.readObject().getJsonObject("quoteSummary").getJsonArray("result").getJsonObject(0);
        JsonObject sD = jo.getJsonObject("summaryDetail");

        StockFinOverview sfo = new StockFinOverview();
        Set<String> keySet =  sD.keySet();

        for(String key: keySet){

            switch (key){
                case "trailingPE":
                    if(sD.getJsonObject("trailingPE").getJsonNumber("raw")!=null){
                        sfo.setTrailingPE(sD.getJsonObject("trailingPE").getJsonNumber("raw").numberValue().floatValue()); }
                    break;

                case "dividendRate":
                    if(sD.getJsonObject("dividendRate").getJsonNumber("raw") != null){
                        sfo.setDividend(sD.getJsonObject("dividendRate").getJsonNumber("raw").numberValue().floatValue()); }
                    break;

                case "exDividendDate":
                    if(sD.getJsonObject("exDividendDate").getJsonString("fmt") != null) {
                        sfo.setExDividendDate(dateFormat.parse(sD.getJsonObject("exDividendDate").getString("fmt"))); }
                    break;

                case "fiveYearAvgDividendYield":
                    if(sD.getJsonObject("fiveYearAvgDividendYield").getJsonNumber("raw") != null){
                        sfo.setAveDivYield5Years(sD.getJsonObject("fiveYearAvgDividendYield").getJsonNumber("raw").numberValue().floatValue()); }
                    break;

                case "payoutRatio":
                    if (sD.getJsonObject("payoutRatio").getJsonNumber("raw") != null) {
                        sfo.setPayoutRatio(sD.getJsonObject("payoutRatio").getJsonNumber("raw").numberValue().floatValue()); }
                    break;
                
                case "beta":
                    if(sD.getJsonObject("beta").getJsonNumber("raw") != null) {
                        sfo.setBeta(sD.getJsonObject("beta").getJsonNumber("raw").numberValue().floatValue()); }
                    break;

                case "marketCap":
                    if(sD.getJsonObject("marketCap").getJsonNumber("raw") != null ){
                        sfo.setMarketCap(sD.getJsonObject("marketCap").getInt("raw")); }
                    break;

                case "fiftyTwoWeekHigh":
                    if (sD.getJsonObject("fiftyTwoWeekHigh").getJsonNumber("raw") != null) {
                        sfo.setFifty2WeekHigh(sD.getJsonObject("fiftyTwoWeekHigh").getJsonNumber("raw").numberValue().floatValue()); }
                    break;

                case "fiftyTwoWeekLow":
                    if(sD.getJsonObject("fiftyTwoWeekLow").getJsonNumber("raw") != null ) {
                        sfo.setFifty2WeekLow(sD.getJsonObject("fiftyTwoWeekLow").getJsonNumber("raw").numberValue().floatValue()); }
                    break;

                case "averageDailyVolume10Day":
                    if(sD.getJsonObject("averageDailyVolume10Day").getJsonNumber("raw") != null ) {
                        sfo.setAverageDailyVol10Days(sD.getJsonObject("averageDailyVolume10Day").getInt("raw")); }
                    break;
                
                case "averageVolume":
                    if(sD.getJsonObject("averageVolume").getJsonNumber("raw") != null ) {
                        sfo.setAverageVol3Months(sD.getJsonObject("averageVolume").getInt("raw")); }
                    break;

                default:
                continue;
            }
        }
        return sfo;
    }

}
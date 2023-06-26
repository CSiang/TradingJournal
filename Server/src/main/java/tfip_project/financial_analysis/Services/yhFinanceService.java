package tfip_project.financial_analysis.Services;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import tfip_project.financial_analysis.Util;
import tfip_project.financial_analysis.Models.SearchResult;

@Service
public class yhFinanceService {
    
    RestTemplate template = new RestTemplate();

    @Autowired
    Util util;

    // To find stock recommendation based on the selected stock. This is for future development use.
    // private final String urlRecommendedSymbols = """
    //     https://query1.finance.yahoo.com/v6/finance/recommendationsbysymbol/aapl """; 

    private final String urlSearchName = """
        https://query1.finance.yahoo.com/v7/finance/autocomplete""";

    private final String urlSearchDetail = """
        https://query2.finance.yahoo.com/v10/finance/quoteSummary""";
    // private final String[] quoteSummaryModuleList = new String[]{"assetProfile","balanceSheetHistory"}; 
    private final String quoteSummaryModuleList = """
        assetProfile,balanceSheetHistory,balanceSheetHistoryQuarterly,calendarEvents,cashflowStatementHistory,cashflowStatementHistoryQuarterly,defaultKeyStatistics,earnings,earningsHistory,earningsTrend,esgScores,financialData,fundOwnership,fundProfile,incomeStatementHistory,incomeStatementHistoryQuarterly,indexTrend,industryTrend,insiderHolders,insiderTransactions,institutionOwnership,majorDirectHolders,majorHoldersBreakdown,netSharePurchaseActivity,price,quoteType,recommendationTrend,secFilings,sectorTrend,summaryDetail,summaryProfile,topHoldings,fundPerformance,upgradeDowngradeHistory,symbol""";    
    
    private final String urlGetStockPrice = """
            https://query2.finance.yahoo.com/v8/finance/chart/
            """;

    public List<SearchResult> findStockByQuery(String query){

        List<SearchResult> resultList = new ArrayList<>();

        String url = UriComponentsBuilder.fromUriString(urlSearchName).queryParam("lang", "en")
                                        .queryParam("query", query)
                                        .build(false)
                                        .toUriString();

        RequestEntity<Void> req = RequestEntity.get(url).build();

        ResponseEntity<String> res = template.exchange(req, String.class);

        if(res.getStatusCode().is2xxSuccessful()){
            String payload = res.getBody();
            JsonReader jr = Json.createReader(new StringReader(payload));
            JsonObject jo = jr.readObject().getJsonObject("ResultSet");
            JsonArray ja = jo.getJsonArray("Result");

            for(int i=0; i< ja.size(); i++){
                jo = ja.getJsonObject(i);

                // Get only Equity. Other type (ETF, crypto, indexes, are filtered off)
                if(jo.getString("typeDisp").equalsIgnoreCase("Equity") && !jo.getString("exchDisp").equalsIgnoreCase("OTC Markets")){
                    SearchResult sr = util.getSearchResult(jo);
                    resultList.add(sr);
                }
            }
        }
        return resultList;
    }


    public String getStockDetails(String symbol){
        String url = UriComponentsBuilder.fromUriString(urlSearchDetail).pathSegment(symbol)
                    .queryParam("modules",quoteSummaryModuleList).toUriString();

        RequestEntity<Void> req = RequestEntity.get(url).build();
        try{
            // Note that if error occurs, restTemplate will throw exception but not giving responseentity, hence need try-catch block. 
            ResponseEntity<String> res = template.exchange(req, String.class);

            return res.getBody();
        } catch(Exception ex){
            System.out.printf("Now throwing exception message...\n");
            ex.printStackTrace();
            return "";
        }
    }

    public  Map<String, String> getStockPrice(List<String> tickerList){

    String firstTicker = tickerList.get(0);
    String tickersString = "";

    if(tickerList.size()>1){
        for(int i=1; i<tickerList.size(); i++){
            tickersString += ("," + tickerList.get(i));
        }
    }
    String url = UriComponentsBuilder.fromUriString(urlGetStockPrice).path(firstTicker)
                                     .queryParam("range", "1d")
                                     .queryParam("interval", "1d")
                                     .queryParam("comparisons", tickersString)
                                     .build(false).toString();
        
    RequestEntity<Void> req = RequestEntity.get(url).build();

    Map<String, String> resultMap = new HashMap<>();

    try{
        ResponseEntity<String> res = template.exchange(req, String.class);

        String payload = res.getBody();
        JsonReader jr = Json.createReader(new StringReader(payload));
        JsonObject jo = jr.readObject().getJsonObject("chart");
        // Here to interrupt, as the jsonobject contains no data. Need to return the empty map ltr.
        if(jo.getJsonArray("result") == null){
            return resultMap;
        }

        JsonObject resultJson = jo.getJsonArray("result").getJsonObject(0);

        // Put in info of 1st ticker...
        resultMap.put( resultJson.getJsonObject("meta").getString("symbol"),
                        resultJson.getJsonObject("meta").getJsonNumber("regularMarketPrice").toString() );

        if(resultJson.containsKey("comparisons")){
            JsonArray otherTickers = resultJson.getJsonArray("comparisons");
            for(int i=0; i<otherTickers.size(); i++){
                jo = otherTickers.getJsonObject(i);
                resultMap.put(jo.getString("symbol"), 
                              jo.getJsonArray("close").getJsonNumber(0).toString() );
            }
        }
        return resultMap;
    } catch(Exception ex){
        ex.printStackTrace();
        return resultMap;
    }
    }


    /* Below is the query for AAPL to get all information. Put here for my reference.
    List of modules that are not needed hence not included: upgradeDowngradeHistory,symbol
    https://query2.finance.yahoo.com/v10/finance/quoteSummary/AAPL?modules=assetProfile,balanceSheetHistory,balanceSheetHistoryQuarterly,calendarEvents,cashflowStatementHistory,cashflowStatementHistoryQuarterly,defaultKeyStatistics,earnings,earningsHistory,earningsTrend,financialData,fundOwnership,incomeStatementHistory,incomeStatementHistoryQuarterly,indexTrend,industryTrend,insiderHolders,insiderTransactions,institutionOwnership,majorDirectHolders,majorHoldersBreakdown,netSharePurchaseActivity,price,quoteType,recommendationTrend,secFilings,sectorTrend,summaryDetail,summaryProfile,fundProfile,topHoldings,fundPerformance */
    /*
    *Below search including news:
     https://query2.finance.yahoo.com/v1/finance/search?q=tesla 
    
    Below to get general info for the symbol:
    * https://query1.finance.yahoo.com/v7/finance/quote?symbols=bs6.si,aapl,frc,NQ=F
    * 
    * Below to find stock by certain keyword, any match even part of the string on symbol or name:
    * https://query1.finance.yahoo.com/v7/finance/autocomplete?query=yangzi&lang=en
    * 
    * 
    * https://query1.finance.yahoo.com/ws/insights/v1/finance/insights?symbol=aapl
    * https://query1.finance.yahoo.com/v7/finance/quote?lang=en-US&region=US&corsDomain=finance.yahoo.com&symbols=aapl
    */





}

package tfip_project.financial_analysis.Services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import tfip_project.financial_analysis.Models.TradingRecord;
import tfip_project.financial_analysis.Repositories.TradeRecordRepository;
import tfip_project.financial_analysis.Security.Models.AppUser;

@Transactional
@Service
public class TradeRecordService {
    
    @Autowired
    UserService userSvc;

    @Autowired
    TradeRecordRepository trRepo;

    @Autowired
    yhFinanceService yhSvc;

    public void save(TradingRecord tRecord){
        AppUser user = userSvc.getUser(tRecord.getUsername());
        tRecord.setUserId(user.getId());
        trRepo.save(tRecord);
    }

    public void saveAll(List<TradingRecord> records){
        TradingRecord firstRecord = records.get(0);
        AppUser user = userSvc.getUser(firstRecord.getUsername());
        Long userId = user.getId();
        for(TradingRecord record: records){
            record.setUserId(userId);
        }
        trRepo.saveAll(records);
    }

    public List<TradingRecord> findByUsername(String username){
        AppUser user = userSvc.getUser(username);
        return trRepo.findByUserId(user.getId());
        // return trRepo.findByUsername(username);
    }

    public void deleteById(Long recordId){
        trRepo.deleteById(recordId);
    }

    public int getRecordCount(Boolean holdingStatus, String username){
        AppUser user = userSvc.getUser(username);
        return trRepo.getRecordCount(holdingStatus, user.getId());
    }

    public List<TradingRecord> getRecords(Boolean holdingStatus, String username
    ,Integer limit, Integer offset) {
        AppUser user = userSvc.getUser(username);
        return trRepo.getRecords(holdingStatus, user.getId(), limit, offset);
    }

    public int getAllRecordCount(String username){
        AppUser user = userSvc.getUser(username);
        return trRepo.getAllRecordCount(user.getId());
    }

    public List<TradingRecord> getAllRecords(String username, Integer limit, Integer offset) {
        AppUser user = userSvc.getUser(username);
        return trRepo.getAllRecords(user.getId(), limit, offset);
    }

    public void deleteRecordById(String username, Long id){
        AppUser user = userSvc.getUser(username);
        Integer result = trRepo.deleteRecordById(user.getId(), id);
        System.out.printf("Record is deleted, result: %d", result);
    }

    public void closeTrade(TradingRecord record){
        trRepo.closeTrade(record.getSellPrice(), record.getSellDate(), 
                        record.getSellTransactionCost(), record.getId());
    }

    public JsonObject getPortfolio(String username){
        AppUser user = userSvc.getUser(username);
        JsonObjectBuilder job = Json.createObjectBuilder();

        // To identify the user that own the summary.
        job.add("username", username);

        String [] statusName = {"hold", "sold"};
        Boolean[] holdingStatus = {true, false};

        List<String> tickerList = new ArrayList<>();
        List<TradingRecord> recordList = new ArrayList<>();

        Map<String, String> resultMap = new HashMap<>();

        for(int i=0; i< statusName.length; i++){
            tickerList = trRepo.getTickersByStatus(holdingStatus[i], user.getId());

            // The max amount of tickers accepted by Yahoo api is 6.
            for(int j=0; j<tickerList.size(); j+=6){
                int endIdx = j+6;
                if( endIdx > tickerList.size()){
                    endIdx =  tickerList.size();
                }
                List<String> subList = tickerList.subList(j, endIdx);
                resultMap.putAll(yhSvc.getStockPrice(subList));
            }

            // ArrayBuilder to contain all summaries of tickers with same holding status
            JsonArrayBuilder jab = Json.createArrayBuilder();

            for(String ticker: tickerList){
                recordList = trRepo.getRecordByStatusAndTicker(holdingStatus[i], ticker, user.getId());

                int stockUnit=0; 
                float stockWorth=0.0f, stockReturn=0.0f;
                String stockName="", currency="";

                for(TradingRecord record: recordList){
                    if(stockUnit == 0){
                        stockName=record.getStockName();
                        currency=record.getCurrency();
                    }
                    stockUnit += record.getBuyAmount();
                    stockWorth += (record.getBuyPrice() * record.getBuyAmount());
                    if(!holdingStatus[i]){
                        stockReturn += (record.getSellPrice() - record.getBuyPrice())*record.getBuyAmount();
                    }
                }
                // Construct JsonObject for summary of each ticker.
                JsonObjectBuilder jsonRecord = Json.createObjectBuilder();
                jsonRecord.add("stockName", stockName)
                          .add("stockTicker",ticker)
                          .add("currency",currency)
                          .add("totalUnit", stockUnit)
                          .add("totalWorth", String.valueOf(
                                new BigDecimal(stockWorth).setScale(2, RoundingMode.HALF_UP)))
                          .add("stockReturn", String.valueOf(
                                 new BigDecimal(stockReturn).setScale(2, RoundingMode.HALF_UP)))
                          .add("stockPrice", resultMap.get(ticker));
                
                // Add the summary of ticker to jsonArray.
                jab.add(jsonRecord);
            }
            // Add the Json Array into final JsonObject.
            job.add(statusName[i],jab);
        }

        return job.build();
    }

}

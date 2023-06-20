package tfip_project.financial_analysis.Controllers;

import java.text.ParseException;
import java.time.Duration;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import tfip_project.financial_analysis.Util;
import tfip_project.financial_analysis.Models.SearchResult;
import tfip_project.financial_analysis.Models.StockFinOverview;
import tfip_project.financial_analysis.Models.StockGenInfo;
import tfip_project.financial_analysis.Models.TradingRecord;
import tfip_project.financial_analysis.Payload.MsgResponse;
import tfip_project.financial_analysis.Services.TradeRecordService;
import tfip_project.financial_analysis.Services.yhFinanceService;

@RestController
@RequestMapping(path = "/api")
public class TradeRestController {
    
    @Autowired
    yhFinanceService yhSvc;

    @Autowired
    Util util;

    @Autowired
    TradeRecordService trSvc;

    @Autowired
    RedisTemplate<String, String> redisTemplate;


    @GetMapping(path = "/search")
    public ResponseEntity<List<SearchResult>> searchStock (@RequestParam String query){

        List<SearchResult> results = yhSvc.findStockByQuery(query);

        if(results.size()>0){
            return ResponseEntity.ok().body(results);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(results);
        }
    }

    @GetMapping(path ="/search/{symbol}")
    public ResponseEntity<StockGenInfo> stockOverview(@PathVariable String symbol) throws ParseException{

        String details ="";
        StockGenInfo genInfo = new StockGenInfo();
        // StockGenInfo genInfo = null;

        StockFinOverview sfo = null;

        try{
            if(redisTemplate.opsForValue().get(symbol) == null){
                details = yhSvc.getStockDetails(symbol);
                redisTemplate.opsForValue().set(symbol, details, Duration.ofDays(1));                
                System.out.printf("Set the Json data for symbol %s into Redis.\n", symbol);
            } else {
                details = redisTemplate.opsForValue().get(symbol);
                System.out.printf("Json data for symbol %s has been obtained from Yahoo and extracted from Redis\n", symbol);
            }
        } catch(Exception ex){
            System.out.println("Redis Template exception...");
            System.out.printf("Error: %s",ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(genInfo);
        }

        if(!details.equals("")){
            System.out.println("Starting to extract genInfo...");
            genInfo = util.jsonToGenInfo(details);
            System.out.println("genInfo is: " + genInfo);
            sfo = util.jsonToStockFinOverview(details);
            System.out.println("sfo is: " + sfo);

            genInfo.setFinOverview(sfo);

            return ResponseEntity.ok().body(genInfo);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(genInfo);
        }
    }

    @PostMapping(path="/saveRecord")
    public ResponseEntity<MsgResponse> saveRecords(@RequestBody List<TradingRecord> tradingRecords){
        
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try{
            for(TradingRecord record: tradingRecords){
                if(!record.getUsername().equals(username)){
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new MsgResponse("Username in the trading records do not match with the user."));
                }
            }
            trSvc.saveAll(tradingRecords);
            return ResponseEntity.ok(new MsgResponse("Saved trading records"));
        } catch(Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MsgResponse(ex.getMessage()));
        }
    }

    // Below return a list of objects.
    // @GetMapping(path="/allRecords/{username}")
    // public ResponseEntity<List<TradingRecord>> getRecords(@PathVariable String username){
    //     return ResponseEntity.ok(trSvc.findByUsername(username));
    // }

    @GetMapping(path="/allRecords/{username}")
    public ResponseEntity<String> getRecords(@PathVariable String username){

        JsonArrayBuilder jab = Json.createArrayBuilder();
        for(TradingRecord record: trSvc.findByUsername(username)){
            jab.add(record.toJson());
        }

        return ResponseEntity.ok(jab.build().toString());
    }


    // public List<TradingRecord> getRecords(Boolean holdingStatus, String username
    // ,Integer limit, Integer offset) 

    @GetMapping(path="/records/{username}")
    public ResponseEntity<String> getRecordsByCriteria(
        @PathVariable String username, 
        @RequestParam(value="holdingStatus", required = false) Boolean holdingStatus,
        @RequestParam(value="limit", defaultValue="20") Integer limit,
        @RequestParam(value="offset", defaultValue="0") Integer offset
    ){
        List<TradingRecord> records;
        JsonObjectBuilder job = Json.createObjectBuilder();

        if(holdingStatus != null){
            job.add("count", trSvc.getRecordCount(holdingStatus, username));
            records = trSvc.getRecords(holdingStatus, username, limit, offset);
        } else {
            job.add("count", trSvc.getAllRecordCount(username));
            records = trSvc.getAllRecords(username, limit, offset);
        }

        JsonArrayBuilder jab = Json.createArrayBuilder();

        for(TradingRecord record: records){
            jab.add(record.toJson());
        }
        job.add("records", jab.build());

        return ResponseEntity.ok(job.build().toString());
    }

    @DeleteMapping(path="/record/{username}/{id}")
    public ResponseEntity<MsgResponse> deleteRecord(
        @PathVariable("username") String username, @PathVariable("id") Long id){

        System.out.println("Delete mapping is activated....");
        System.out.printf("Variables are: username= %s, id= %d\n", username, id);

        try{
            trSvc.deleteRecordById(username, id);
            return ResponseEntity.ok(new MsgResponse("Record is deleted."));
        } catch (Exception ex){
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MsgResponse(ex.getMessage()));
        }
    }

    @PutMapping(path="/closeRecord")
    public ResponseEntity<MsgResponse> closeRecord(@RequestBody TradingRecord record){

        System.out.println("Record received from FE for update is: " + record);

        try {
            trSvc.closeTrade(record);
            return ResponseEntity.ok(new MsgResponse("Record is closed."));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MsgResponse(ex.getMessage()));
        }
    }

    @GetMapping(path="/tradeSummary")
    public ResponseEntity<String> getTradeSummary(){
        String username = (String)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(trSvc.getPortfolio(username).toString());
    }



}

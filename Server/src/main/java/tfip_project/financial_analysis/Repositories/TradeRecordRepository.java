package tfip_project.financial_analysis.Repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import tfip_project.financial_analysis.Models.TradingRecord;

public interface TradeRecordRepository extends JpaRepository<TradingRecord, Long>{

    final String GET_COUNTS_BY_STATUS = """
                                select count(*) from trading_record
                                where holding_status = ? and user_id= ?;
                                """;

    final String GET_RECORDS_BY_STATUS = """
                                select * from trading_record
                                where holding_status = ? and user_id = ?
                                limit ? offset ? ; """;

    final String GET_ALL_COUNTS = """
                                    select count(*) from trading_record
                                    where user_id = ?; """;

    final String GET_ALL_RECORDS = """
                                    select * from trading_record
                                    where user_id = ?
                                    limit ? offset ? ; """;

    final String DELETE_BY_ID = """
                    delete from trading_record where user_id = ? and id = ?; """;

        // delete from trading_record where username = 'CS' and id= 16;

    final String CLOSE_TRADE = """
                    update trading_record 
                    set sell_price = ?, sell_date = ?, sell_transaction_cost = ?, 
                    holding_status = false
                    where id= ?; """;

    final String GET_DISTINCT_STOCK_TICKERS_BY_STATUS = """
                    select distinct stock_ticker from trading_record
                    where holding_status = ? and user_id = ?
                    order by stock_ticker asc """;

    final String GET_RECORDS_BY_STATUS_AND_TICKER= """
                    select * from trading_record
                    where holding_status = ? and stock_ticker= ? and user_id = ? """;


    // public List<TradingRecord> findByUsername(String username);

    public List<TradingRecord> findByUserId(Long userId);

    // Must use nativeQuery = true.
    @Query(value =GET_COUNTS_BY_STATUS, nativeQuery = true)
    public int getRecordCount(Boolean holdingStatus, Long userId);

    @Query(value=GET_RECORDS_BY_STATUS, nativeQuery = true)
    public List<TradingRecord> getRecords(Boolean holdingStatus, Long userId
    ,Integer limit, Integer offset);

    @Query(value =GET_ALL_COUNTS, nativeQuery = true)
    public int getAllRecordCount(Long userId);

    @Query(value=GET_ALL_RECORDS, nativeQuery = true)
    public List<TradingRecord> getAllRecords(Long userId,Integer limit, Integer offset);

    @Modifying
    @Query(value=DELETE_BY_ID, nativeQuery = true)
    public Integer deleteRecordById(Long userId, Long id);

    @Modifying
    @Query(value=CLOSE_TRADE, nativeQuery = true)
    public void closeTrade(Float sellPrice, Date sellDate, Float sellTransactionCost, Long id);

    @Query(value= GET_DISTINCT_STOCK_TICKERS_BY_STATUS, nativeQuery = true)
    public List<String> getTickersByStatus(Boolean holdingStatus, Long userId);

    @Query(value= GET_RECORDS_BY_STATUS_AND_TICKER, nativeQuery = true)
    public List<TradingRecord> getRecordByStatusAndTicker(Boolean holdingStatus, String ticker, Long userId);

}

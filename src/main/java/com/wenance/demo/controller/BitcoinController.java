package com.wenance.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wenance.demo.model.BitcoinPrice;
import com.wenance.demo.model.BitcoinStats;
import com.wenance.demo.service.BitcoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

@Configuration
@RestController
public class BitcoinController {

    @Autowired
    private BitcoinService bitcoinService;

    @RequestMapping("/bitcoin/getRatioByTime")
    public ResponseEntity getBitcoinByTime(@RequestParam(value = "timestamp", defaultValue = "") String timestamp) throws JsonProcessingException {

        LocalDateTime inputTime = LocalDateTime.parse(timestamp);
        System.out.println(inputTime.toString());

        BitcoinPrice foundPrice = bitcoinService.getBitcoinByTime(inputTime);

        return ResponseEntity.ok(foundPrice);
    }


    @RequestMapping("/bitcoin/getBitcoinTrends")
    public ResponseEntity getBitcoinTrends(@RequestParam(value = "dateFrom", defaultValue = "") String dateFrom,
                                   @RequestParam(value = "dateTo", defaultValue = "") String dateTo) throws JsonProcessingException {

        BitcoinStats bitcoinStats = bitcoinService.getBitcoinTrends(
                        LocalDateTime.parse(dateFrom),
                        LocalDateTime.parse(dateTo));

        return ResponseEntity.ok(bitcoinStats);
    }


    private ArrayList<BitcoinPrice> getMockList(){
        ArrayList<BitcoinPrice> retList = new ArrayList<BitcoinPrice>();
        double[] lprices = { 44194.0, 40472.7, 40747.9,
                40676.4, 40927.5, 47336.3, 41290.5, 45472.9, 44778.3, 44902.9,
                46528.8, 45133.7, 43105.7, 46306.0, 48485.1, 42041.9, 46206.6,
                43896.9, 46756.6, 40230.7, 48273.3, 42544.1, 42064.7, 43137.7,
                47061.0, 43055.3, 46899.6, 44433.4, 41692.7, 45568.7, 40369.6,
                44797.0, 41133.1, 43897.9, 47756.4, 44206.0, 42520.7, 40365.3,
                40730.2, 45939.5, 42676.3, 48691.4, 42254.6, 42879.1, 43455.9,
                42742.4, 41583.8, 44822.5, 43788.6, 48201.4};

        LocalDateTime timestamp = LocalDateTime.now();
        for(int i=0; i<50; i++){
            BitcoinPrice bccr = new BitcoinPrice(lprices[i], "BTC", "USD", timestamp);
            retList.add(bccr);
            System.out.println(bccr.toString());
            timestamp = timestamp.plusSeconds(10);
        }
        return retList;
    }

}

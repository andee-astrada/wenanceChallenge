package com.wenance.demo.service;

import com.wenance.demo.BitcoinDemoApplication;
import com.wenance.demo.model.BitcoinHistory;
import com.wenance.demo.model.BitcoinPrice;
import com.wenance.demo.model.BitcoinStats;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.OptionalDouble;

@Service
public class BitcoinService {
    private final BitcoinHistory bitcoinHistory;

    public BitcoinService(BitcoinHistory bitcoinHistory){
        this.bitcoinHistory = bitcoinHistory;
    }

    public BitcoinPrice getBitcoinByTime(LocalDateTime dt){
        LinkedList<BitcoinPrice> priceLog = bitcoinHistory.getBitcoinHistory();

        BitcoinPrice foundPrice =
                priceLog.stream().filter(prices -> prices.getTimestamp().isBefore(dt))
                        .filter(prices -> prices.getTimestamp().plusSeconds(10).isAfter(dt)).findFirst().get();

        return foundPrice;
    }

    public  BitcoinStats getBitcoinTrends(LocalDateTime dateFrom, LocalDateTime dateTo){
        LinkedList<BitcoinPrice> priceLog = bitcoinHistory.getBitcoinHistory();

        BitcoinStats bitcoinStats = new BitcoinStats(dateFrom, dateTo);

        double maxValue =
                priceLog.stream().max(Comparator.comparing(BitcoinPrice::getLprice)).get().getLprice();
        System.out.println("MaxValue: " + maxValue);

        OptionalDouble avg =
                priceLog.stream()
                        .filter(ratios -> ratios.getTimestamp().isAfter(bitcoinStats.getDateFrom()))
                        .filter(ratios -> ratios.getTimestamp().isBefore(bitcoinStats.getDateTo()))
                        .mapToDouble(BitcoinPrice::getLprice).average();

        if(avg.isPresent()){
            bitcoinStats.setAvgValue(avg.getAsDouble());
            System.out.println("Avg: " + avg.getAsDouble());
        }
        bitcoinStats.setPercentageAgainstMaxValue(maxValue);
        System.out.println("Perc: " + bitcoinStats.getPercentageAgainstMaxValue());


        return bitcoinStats;
    }
}

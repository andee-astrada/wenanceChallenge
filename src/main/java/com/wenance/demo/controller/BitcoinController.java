package com.wenance.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wenance.demo.model.BitcoinPrice;
import com.wenance.demo.model.BitcoinStats;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Configuration
@RestController
public class BitcoinController {
    private ArrayList<BitcoinPrice> bitcoinPrices = new ArrayList<BitcoinPrice>();

    @Scheduled(fixedRate = 10000)
    public void retrieveBitcoinConvertionRatio(){

        String endpoint = "https://cex.io/api/last_price/BTC/USD";

        //inicializarlo en el contxto y pedirlo como parametro
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        BitcoinPrice response = restTemplate.exchange(endpoint, HttpMethod.GET, entity, BitcoinPrice.class).getBody();

        System.out.println(response.toString());
        bitcoinPrices.add(response);
    }

    @RequestMapping("/bitcoin/getRatioByTime")
    public String getBitcoinByTime(@RequestParam(value = "timestamp", defaultValue = "") String timestamp) throws JsonProcessingException {

        LocalDateTime inputTime = LocalDateTime.parse(timestamp);
        System.out.println(inputTime.toString());

        //ArrayList<BitcoinConvertionRatio> convertionLog = getMockList() ;
        ArrayList<BitcoinPrice> convertionLog = bitcoinPrices;

        BitcoinPrice foundRatio =
                convertionLog.stream().filter(ratios -> ratios.getTimestamp().isBefore(inputTime))
                        .filter(ratios -> ratios.getTimestamp().plusSeconds(10).isAfter(inputTime)).findFirst().get();

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(foundRatio);
        //return String.valueOf(foundRatio.getLprice());
    }


    @RequestMapping("/bitcoin/getBitcoinTrends")
    public String getBitcoinTrends(@RequestParam(value = "dateFrom", defaultValue = "") String dateFrom,
                                   @RequestParam(value = "dateTo", defaultValue = "") String dateTo) throws JsonProcessingException {

        BitcoinStats bitcoinStats = new BitcoinStats();
        bitcoinStats.setDateFrom(LocalDateTime.parse(dateFrom));
        bitcoinStats.setDateTo(LocalDateTime.parse(dateTo));

        //ArrayList<BitcoinConvertionRatio> convertionLog = getMockList() ;
        ArrayList<BitcoinPrice> convertionLog = bitcoinPrices;

        double maxValue =
                convertionLog.stream().max(Comparator.comparing(BitcoinPrice::getLprice)).get().getLprice();
        System.out.println("MaxValue: " + maxValue);
        OptionalDouble avg =
                convertionLog.stream()
                        .filter(ratios -> ratios.getTimestamp().isAfter(bitcoinStats.getDateFrom()))
                        .filter(ratios -> ratios.getTimestamp().isBefore(bitcoinStats.getDateTo()))
                        .mapToDouble(BitcoinPrice::getLprice).average();

        if(avg.isPresent()){
            bitcoinStats.setAvgValue(avg.getAsDouble());
            System.out.println("Avg: " + avg.getAsDouble());
        }
        bitcoinStats.setPercentageAgainstMaxValue(maxValue);
        System.out.println("Perc: " + bitcoinStats.getPercentageAgainstMaxValue());

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(bitcoinStats);
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

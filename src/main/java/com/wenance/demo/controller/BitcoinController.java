package com.wenance.demo.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.wenance.demo.model.BitcoinPrice;
import com.wenance.demo.model.BitcoinStats;
import com.wenance.demo.service.BitcoinService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@Configuration
@RestController
@RequestMapping("/bitcoin")
public class BitcoinController {

//    @Autowired
    private final BitcoinService bitcoinService;

    public BitcoinController(BitcoinService bitcoinService){
        this.bitcoinService = bitcoinService;
    }

    @GetMapping("/prices")
    public ResponseEntity getBitcoinByTime(@RequestParam(value = "timestamp", defaultValue = "") String timestamp)  {

        LocalDateTime inputTime;

        try {
            inputTime = LocalDateTime.parse(timestamp);

            if(inputTime.isAfter(LocalDateTime.now()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Input date cannot happen in the future");

        } catch (DateTimeParseException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format, please use yyyy-MM-ddTHH:mm:ss");
        }

        BitcoinPrice foundPrice = bitcoinService.getBitcoinByTime(inputTime);

        if(foundPrice != null)
            return ResponseEntity.ok(foundPrice);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No data for the time specified");
    }


    @GetMapping("/trends")
    public ResponseEntity getBitcoinTrends(@RequestParam(value = "dateFrom", defaultValue = "") String dateFrom,
                                   @RequestParam(value = "dateTo", defaultValue = "") String dateTo) {
        LocalDateTime localDateFrom, localDateTo;

        try{
            localDateFrom = LocalDateTime.parse(dateFrom);
            localDateTo = LocalDateTime.parse(dateTo);

            if(localDateFrom.isAfter(LocalDateTime.now()))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("From date cannot happen in the future");
            if(localDateFrom.isAfter(localDateTo))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid Range");

        } catch (DateTimeParseException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid date format, please use yyyy-MM-ddTHH:mm:ss");
        }

        BitcoinStats bitcoinStats = bitcoinService.getBitcoinTrends(
                LocalDateTime.parse(dateFrom),
                LocalDateTime.parse(dateTo));

        if(bitcoinStats != null)
            return ResponseEntity.ok(bitcoinStats);
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No results for the time range specified");
    }

}

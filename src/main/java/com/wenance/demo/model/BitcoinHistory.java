package com.wenance.demo.model;

import com.wenance.demo.controller.BitcoinController;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

@Component
public class BitcoinHistory {
    private LinkedList<BitcoinPrice> bitcoinHistory;

    public BitcoinHistory () {
        System.out.println("BitcoinHistory created");
        bitcoinHistory = new LinkedList<BitcoinPrice>();
    }

    public void addBitcoinPrice(BitcoinPrice bp){
        bitcoinHistory.add(bp);
    }

    public LinkedList<BitcoinPrice> getBitcoinHistory(){
        return bitcoinHistory;
    }
}

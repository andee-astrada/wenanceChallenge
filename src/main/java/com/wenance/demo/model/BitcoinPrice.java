package com.wenance.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BitcoinPrice {
        private double lprice;
        private String curr1;
        private String curr2;

        @JsonSerialize(using = LocalDateTimeSerializer.class)
        @JsonDeserialize(using = LocalDateTimeDeserializer.class)
        private LocalDateTime timestamp;

        public BitcoinPrice() {
            timestamp = LocalDateTime.now();
        }

        public BitcoinPrice(double lprice, String curr1, String curr2, LocalDateTime timestamp) {
            this.lprice = lprice;
            this.curr1 = curr1;
            this.curr2 = curr2;
            this.timestamp = timestamp;
        }

        public double getLprice() {
            return lprice;
        }

        public void setLprice(double lprice) {
            this.lprice = lprice;
        }

        public String getCurr1() {
            return curr1;
        }

        public void setCurr1(String curr1) {
            this.curr1 = curr1;
        }

        public String getCurr2() {
            return curr2;
        }

        public void setCurr2(String curr2) {
            this.curr2 = curr2;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String toString(){
            return this.timestamp.toString() + " - " + lprice;
        }

    }

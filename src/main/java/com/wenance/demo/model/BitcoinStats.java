package com.wenance.demo.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BitcoinStats {
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateFrom;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime dateTo;

    private double maxValue;
    private double avgValue;
    private double percentageAgainstMaxValue;

    public BitcoinStats(LocalDateTime dateFrom, LocalDateTime dateTo){
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }

    public BitcoinStats(){

    }

    public LocalDateTime getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDateTime dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDateTime getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDateTime dateTo) {
        this.dateTo = dateTo;
    }

    public double getAvgValue() {
        return avgValue;
    }

    public void setAvgValue(double avgValue) {
        this.avgValue = avgValue;
        if(this.maxValue!=0 && avgValue!=0)
            calculatePercentageAgainstMaxValue();
    }

    public double getPercentageAgainstMaxValue() {
        return percentageAgainstMaxValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(double maxValue) {
        this.maxValue = maxValue;
        if(this.maxValue!=0 && avgValue!=0)
            calculatePercentageAgainstMaxValue();
    }

    private void calculatePercentageAgainstMaxValue() {
        percentageAgainstMaxValue = round((avgValue*100/maxValue)-100);
    }

    private double round(double value) {
        long factor = (long) Math.pow(10, 2);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
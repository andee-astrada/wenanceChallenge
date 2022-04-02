package com.wenance.demo.service;

import com.wenance.demo.model.BitcoinHistory;
import com.wenance.demo.model.BitcoinPrice;
import com.wenance.demo.model.BitcoinStats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.LinkedList;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class)
class BitcoinServiceTest {
    private final String TEST_DATE = "2022-04-02T00:28:01.001";
    private final LinkedList<BitcoinPrice> bitcoinList = getMockHistory();

    @InjectMocks
    private BitcoinService bitcoinService;

    @Mock
    private BitcoinHistory bitcoinHistory;

    @Test
    @DisplayName("getBitcoinByTime - OK")
    public void getBitcoinByTime() {
        //Given
        LocalDateTime dt = LocalDateTime.parse(TEST_DATE)
            .plus(5000, ChronoField.MILLI_OF_DAY.getBaseUnit());

        when(bitcoinHistory.getBitcoinHistory()).thenReturn(bitcoinList);

        //when
        BitcoinPrice result = bitcoinService.getBitcoinByTime(dt);

        //assert
        Assertions.assertNotNull(result);
    }

    @Test
    @DisplayName("getBitcoinByTime - lower date test")
    public void getBitcoinByTimeMinLimit() {
        //Given
        LocalDateTime dt = LocalDateTime.parse(TEST_DATE)
                .plus(1, ChronoField.MILLI_OF_DAY.getBaseUnit());

        when(bitcoinHistory.getBitcoinHistory()).thenReturn(bitcoinList);

        //when
        BitcoinPrice result = bitcoinService.getBitcoinByTime(dt);

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getLprice(), bitcoinList.get(0).getLprice());
    }

    @Test
    @DisplayName("getBitcoinTrends - OK")
    void getBitcoinTrendsRange() {
        //Given
        LocalDateTime dtFrom = LocalDateTime.parse(TEST_DATE)
                .plus(1, ChronoField.MILLI_OF_DAY.getBaseUnit());
        LocalDateTime dtTo = LocalDateTime.parse(TEST_DATE)
                .plus(2, ChronoField.MINUTE_OF_DAY.getBaseUnit());

//        System.out.println(dtFrom.toString());
//        System.out.println(dtTo.toString());

        when(bitcoinHistory.getBitcoinHistory()).thenReturn(bitcoinList);

        //when
        BitcoinStats result = bitcoinService.getBitcoinTrends(dtFrom, dtTo);

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(43478.9, result.getAvgValue());
        Assertions.assertEquals(-10.71, result.getPercentageAgainstMaxValue()) ;

    }

    @Test
    @DisplayName("getBitcoinTrends - Time range exceeded")
    void getBitcoinTrendsExceedingRange() {
        //Given
        LocalDateTime dtFrom = LocalDateTime.parse(TEST_DATE)
                .minus(365, ChronoField.DAY_OF_YEAR.getBaseUnit());
        LocalDateTime dtTo = LocalDateTime.parse(TEST_DATE)
                .plus(365, ChronoField.DAY_OF_YEAR.getBaseUnit());

        when(bitcoinHistory.getBitcoinHistory()).thenReturn(bitcoinList);

        //when
        BitcoinStats result = bitcoinService.getBitcoinTrends(dtFrom, dtTo);

        //assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals(43940.672, result.getAvgValue());
        Assertions.assertEquals(-9.76, result.getPercentageAgainstMaxValue()) ;

    }

    @Test
    @DisplayName("getBitcoinTrends - Time range in the past, no data")
    void getBitcoinTrendsNoResultsPast() {
        //Given
        LocalDateTime dtFrom = LocalDateTime.parse(TEST_DATE)
                .minus(365, ChronoField.DAY_OF_YEAR.getBaseUnit());
        LocalDateTime dtTo = LocalDateTime.parse(TEST_DATE)
                .minus(265, ChronoField.DAY_OF_YEAR.getBaseUnit());

        when(bitcoinHistory.getBitcoinHistory()).thenReturn(bitcoinList);

        //when
        BitcoinStats result = bitcoinService.getBitcoinTrends(dtFrom, dtTo);

        //assert
        Assertions.assertNull(result);
    }

    @Test
    @DisplayName("getBitcoinTrends - Time range in the future, no data")
    void getBitcoinTrendsNoResultsFuture() {
        //Given
        LocalDateTime dtFrom = LocalDateTime.parse(TEST_DATE)
                .plus(265, ChronoField.DAY_OF_YEAR.getBaseUnit());
        LocalDateTime dtTo = LocalDateTime.parse(TEST_DATE)
                .plus(365, ChronoField.DAY_OF_YEAR.getBaseUnit());

        when(bitcoinHistory.getBitcoinHistory()).thenReturn(bitcoinList);

        //when
        BitcoinStats result = bitcoinService.getBitcoinTrends(dtFrom, dtTo);

        //assert
        Assertions.assertNull(result);
    }

    private LinkedList<BitcoinPrice> getMockHistory(){
        LinkedList<BitcoinPrice> retList = new LinkedList<BitcoinPrice>();
        double[] lprices = { 44194.0, 40472.7, 40747.9,
                40676.4, 40927.5, 47336.3, 41290.5, 45472.9, 44778.3, 44902.9,
                46528.8, 45133.7, 43105.7, 46306.0, 48485.1, 42041.9, 46206.6,
                43896.9, 46756.6, 40230.7, 48273.3, 42544.1, 42064.7, 43137.7,
                47061.0, 43055.3, 46899.6, 44433.4, 41692.7, 45568.7, 40369.6,
                44797.0, 41133.1, 43897.9, 47756.4, 44206.0, 42520.7, 40365.3,
                40730.2, 45939.5, 42676.3, 48691.4, 42254.6, 42879.1, 43455.9,
                42742.4, 41583.8, 44822.5, 43788.6, 48201.4};

        LocalDateTime timestamp = LocalDateTime.parse(TEST_DATE);
        for(int i=0; i<50; i++){
            BitcoinPrice bccr = new BitcoinPrice(lprices[i], "BTC", "USD", timestamp);
            retList.add(bccr);
            //System.out.println(bccr.toString());
            timestamp = timestamp.plusSeconds(10);
        }
        return retList;
    }


}
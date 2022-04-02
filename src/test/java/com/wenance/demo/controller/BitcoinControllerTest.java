package com.wenance.demo.controller;

import com.wenance.demo.model.BitcoinPrice;
import com.wenance.demo.model.BitcoinStats;
import com.wenance.demo.service.BitcoinService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BitcoinControllerTest {
    private final BitcoinPrice bitcoinPrice = getMockPrice();

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private BitcoinController bitcoinController;

    @MockBean
    private BitcoinService bitcoinService;

    @Test
    @DisplayName("/getPriceByTime - OK")
    void getBitcoinByFormattedTime() throws Exception {
        LocalDateTime inputTime = LocalDateTime.now();
        String timestamp = "2021-04-01T17:44:01";

        when(bitcoinService.getBitcoinByTime(Mockito.any(LocalDateTime.class))).thenReturn(bitcoinPrice);

        mockMvc.perform(get("/bitcoin/getPriceByTime?timestamp="+timestamp))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.curr1", is("BTC")))
                .andExpect(jsonPath("$.curr2", is("USD")))
                .andExpect(jsonPath("$.lprice", is(42333.1)));
    }


    @Test
    @DisplayName("/getPriceByTime - Wrong date format")
    void getBitcoinByNonFormattedTime() throws Exception {
        LocalDateTime inputTime = LocalDateTime.now();
        String timestamp = "____-04-01T17:ZZ:01";

        when(bitcoinService.getBitcoinByTime(Mockito.any(LocalDateTime.class))).thenReturn(bitcoinPrice);

        mockMvc.perform(get("/bitcoin/getPriceByTime?timestamp="+timestamp))
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("/getPriceByTime - Future date")
    void getBitcoinByFutureTime() throws Exception {
        LocalDateTime inputTime = LocalDateTime.now();
        String timestamp = "2030-04-01T17:00:00";

        when(bitcoinService.getBitcoinByTime(Mockito.any(LocalDateTime.class))).thenReturn(bitcoinPrice);

        mockMvc.perform(get("/bitcoin/getPriceByTime?timestamp="+timestamp))
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("/getPriceByTime - No results")
    void getBitcoinByPastTime() throws Exception {
        LocalDateTime inputTime = LocalDateTime.now();
        String timestamp = "2010-04-01T17:00:00";

        when(bitcoinService.getBitcoinByTime(Mockito.any(LocalDateTime.class))).thenReturn(null);

        mockMvc.perform(get("/bitcoin/getPriceByTime?timestamp="+timestamp))
                .andExpect(status().is(404));
    }

    @Test
    @DisplayName("/getBitcoinTrends - OK")
    void getBitcoinTrendsFormattedTime() throws Exception {
        LocalDateTime inputTime = LocalDateTime.now();
        String dateFrom = "2021-04-01T17:44:01";
        String dateTo   = "2021-04-01T18:44:01";

        BitcoinStats bitcoinStats = getMockStats(dateFrom, dateTo);

        when(bitcoinService.getBitcoinTrends(Mockito.any(LocalDateTime.class),Mockito.any(LocalDateTime.class)))
            .thenReturn(bitcoinStats);

        mockMvc.perform(get("/bitcoin/getBitcoinTrends?" +
                        "dateFrom=" + dateFrom +
                        "&dateTo=" + dateTo))
                // Validate the response code and content type
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Validate the returned fields
                .andExpect(jsonPath("$.dateFrom", is("2021-04-01T17:44:01")))
                .andExpect(jsonPath("$.dateTo",   is("2021-04-01T18:44:01")))
                .andExpect(jsonPath("$.avgValue", is(40000.0)))
                .andExpect(jsonPath("$.maxValue", is(45000.0)))
                .andExpect(jsonPath("$.percentageAgainstMaxValue", is(-11.11)));
    }

    @Test
    @DisplayName("/getBitcoinTrends - Wrong FROM date format")
    void getBitcoinTrendsNonFormattedFromTime() throws Exception {
        LocalDateTime inputTime = LocalDateTime.now();
        String dateFrom = "2021-__-01T17:44:01zzz";
        String dateTo   = "2021-04-01T18:44:01";

        when(bitcoinService.getBitcoinTrends(Mockito.any(LocalDateTime.class),Mockito.any(LocalDateTime.class)))
                .thenReturn(null);

        mockMvc.perform(get("/bitcoin/getBitcoinTrends?" +
                        "dateFrom=" + dateFrom +
                        "&dateTo=" + dateTo))
                // Validate the response code and content type
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("/getBitcoinTrends - Wrong TO date format")
    void getBitcoinTrendsNonFormattedToTime() throws Exception {
        LocalDateTime inputTime = LocalDateTime.now();
        String dateFrom = "2021-__-01T17:44:01zzz";
        String dateTo   = "2021-04-01T18:44:01";

        when(bitcoinService.getBitcoinTrends(Mockito.any(LocalDateTime.class),Mockito.any(LocalDateTime.class)))
                .thenReturn(null);

        mockMvc.perform(get("/bitcoin/getBitcoinTrends?" +
                        "dateFrom=" + dateFrom +
                        "&dateTo=" + dateTo))
                // Validate the response code and content type
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("/getBitcoinTrends - Invalid date range")
    void getBitcoinTrendsInvalidRange() throws Exception {
        LocalDateTime inputTime = LocalDateTime.now();
        String dateFrom = "2023-04-01T18:44:01";
        String dateTo   = "2021-04-01T18:44:01";

        when(bitcoinService.getBitcoinTrends(Mockito.any(LocalDateTime.class),Mockito.any(LocalDateTime.class)))
                .thenReturn(null);

        mockMvc.perform(get("/bitcoin/getBitcoinTrends?" +
                        "dateFrom=" + dateFrom +
                        "&dateTo=" + dateTo))
                // Validate the response code and content type
                .andExpect(status().is(400));
    }

    @Test
    @DisplayName("/getBitcoinTrends - No results")
    void getBitcoinTrendsEmptyRange() throws Exception {
        LocalDateTime inputTime = LocalDateTime.now();
        String dateFrom = "2010-04-01T18:44:01";
        String dateTo   = "2015-04-01T18:44:01";

        when(bitcoinService.getBitcoinTrends(Mockito.any(LocalDateTime.class),Mockito.any(LocalDateTime.class)))
                .thenReturn(null);

        mockMvc.perform(get("/bitcoin/getBitcoinTrends?" +
                        "dateFrom=" + dateFrom +
                        "&dateTo=" + dateTo))
                // Validate the response code and content type
                .andExpect(status().is(404));
    }

    private BitcoinPrice getMockPrice() {
        return new BitcoinPrice(42333.1, "BTC", "USD", LocalDateTime.now());
    }

    private BitcoinStats getMockStats(String dateFrom, String dateTo) {
        BitcoinStats bitcoinStats = new BitcoinStats(LocalDateTime.parse(dateFrom), LocalDateTime.parse(dateTo));
        bitcoinStats.setMaxValue(45000);
        bitcoinStats.setAvgValue(40000);
        return bitcoinStats;
    }

}
package com.wenance.demo.schedule;

import com.wenance.demo.model.BitcoinHistory;
import com.wenance.demo.model.BitcoinPrice;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
public class BitcoinSchedule {

    @Value("${endpoint}")
    private String endpoint;

    private final BitcoinHistory bitcoinHistory;
    private final RestTemplate restTemplate;

    public BitcoinSchedule(BitcoinHistory bitcoinHistory, RestTemplate restTemplate){
        this.bitcoinHistory = bitcoinHistory;
        this.restTemplate = restTemplate;
    }

    @Scheduled(fixedRate = 10000)
    public void retrieveBitcoinConvertionRatio(){

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);

        HttpHeaders headers = new HttpHeaders();
        headers.add("user-agent", "Application");
        HttpEntity<String> entity = new HttpEntity<>(headers);

        Optional<BitcoinPrice> response = Optional.ofNullable(restTemplate.exchange(endpoint, HttpMethod.GET, entity, BitcoinPrice.class).getBody());

        if (response.isPresent()) {
            bitcoinHistory.addBitcoinPrice(response.get());
            System.out.println(response.get().toString());
        }
    }

}

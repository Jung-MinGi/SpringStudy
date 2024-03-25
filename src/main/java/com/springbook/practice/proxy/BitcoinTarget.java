package com.springbook.practice.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

public class BitcoinTarget implements Bitcoin {
    @Override
    public int getPrice() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String forObject = restTemplate.getForObject(new URI("https://api.upbit.com/v1/ticker?markets=KRW-BTC"), String.class);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(forObject);
            String s = jsonNode.findValuesAsText("trade_price").get(0);
            double l = Double.parseDouble(s);
            return (int) l;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}

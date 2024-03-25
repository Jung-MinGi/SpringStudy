package com.springbook.practice.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.net.URISyntaxException;

public interface Bitcoin {
    int getPrice() throws JsonProcessingException, URISyntaxException;
}

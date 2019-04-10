package com.igor.web_scraper.scraper;

import lombok.Value;

@Value
public class Product {

    private String name;

    private String price;

    private byte[] image;
}

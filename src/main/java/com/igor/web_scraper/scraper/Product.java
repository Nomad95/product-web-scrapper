package com.igor.web_scraper.scraper;

import lombok.ToString;
import lombok.Value;

@Value
@ToString(exclude = "image")
public class Product {
    private String name;
    private String price;
    private byte[] image;
}

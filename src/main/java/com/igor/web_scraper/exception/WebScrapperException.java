package com.igor.web_scraper.exception;

public class WebScrapperException extends RuntimeException {

    public WebScrapperException(String message) {
        super(message);
    }

    public WebScrapperException(String message, Throwable cause) {
        super(message, cause);
    }
}

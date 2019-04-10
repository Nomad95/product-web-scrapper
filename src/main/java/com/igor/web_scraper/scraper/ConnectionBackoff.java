package com.igor.web_scraper.scraper;

import com.igor.web_scraper.exception.WebScrapperException;
import lombok.extern.slf4j.Slf4j;

import java.net.SocketTimeoutException;

import static com.igor.web_scraper.WebScraper.notifyUser;

@Slf4j
public class ConnectionBackoff {

    public void tryRetryConnection(int retryThreshold, SocketTimeoutException e) {
        if (retryThreshold == 0)
            throw new WebScrapperException("Could not connect to host", e);
        log.debug("Connection timeout. Retrying in 1 second");
        notifyUser("Could not connect to host. Retrying in 1 second...");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            log.error("Application error {}", ex);
            throw new WebScrapperException("Application error");
        }
    }

}

package com.igor.web_scraper.scraper;

import com.igor.web_scraper.exception.WebScrapperException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

@Slf4j
@RequiredArgsConstructor
public class SiteConnector {

    private static final int DEFAULT_RETRY_THRESHOLD = 3;

    private final ConnectionBackoff connectionBackoff;

    public Connection getNewConnection(String url) {
        return Jsoup.connect(url);
    }

    public void addStandardDisguiseHeaders(Connection connection) {
        connection.header("Connection", "keep-alive");
        connection.header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        connection.header("Accept-Encoding", "gzip, deflate, sdch");
        connection.header("Accept-Language", "pl-PL,pl;q=0.9,en-US;q=0.8,en;q=0.7");
        connection.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.95 Safari/537.36");
        connection.referrer("https://www.google.com/");
    }

    public void setHost(Connection connection, String host) {
        connection.header("Host", host);
    }

    public Document getDocument(Connection connection) {
        return getDocument(connection, DEFAULT_RETRY_THRESHOLD);
    }

    private Document getDocument(Connection connection, int retryThreshold) {
        try {
            return connection.get();
        } catch (HttpStatusException e) {
            return handleHttpError(e);
        } catch (SocketTimeoutException e) {
            return handleTimeoutError(connection, retryThreshold, e);
        } catch (UnknownHostException e) {
            throw new WebScrapperException("Could not connect to host. Check your internet connection", e);
        } catch (IOException e) {
            throw new WebScrapperException("Could not connect to host", e);
        }
    }

    private Document handleHttpError(HttpStatusException e) {
        if (e.getStatusCode() == 404) {
            throw new WebScrapperException("Site " + e.getUrl() + " was not found", e);
        } else {
            throw new WebScrapperException("Server responded with HTTP " + e.getStatusCode() + " at url: " + e.getUrl(), e);
        }
    }

    private Document handleTimeoutError(Connection connection, int retryThreshold, SocketTimeoutException e) {
        connectionBackoff.tryRetryConnection(retryThreshold, e);
        return getDocument(connection, retryThreshold - 1);
    }

    public byte[] safeGetContentAsBytes(String url) {
        try {
            return Jsoup.connect(url).ignoreContentType(true).execute().bodyAsBytes();
        } catch (IOException e) {
            log.warn("Could not get content at {}", url);
            log.debug("Thrown exception: {}", e);
            return new byte[0];
        }
    }

}

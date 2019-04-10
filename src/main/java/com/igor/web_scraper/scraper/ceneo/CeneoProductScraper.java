package com.igor.web_scraper.scraper.ceneo;

import com.igor.web_scraper.exception.WebScrapperException;
import com.igor.web_scraper.scraper.Product;
import com.igor.web_scraper.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class CeneoProductScraper implements Scraper<Product> {

    private final CeneoProductExtractor ceneoProductExtractor;

    @Override
    public List<Product> scrapSite(String startUrl) {
//        Connection connection = siteConnector.getNewConnection(startUrl);
//        siteConnector.addStandardDisguiseHeaders(connection);
//        siteConnector.setHost(connection, HOST);
//        Document html = siteConnector.getDocument(connection);

        List<Product> products = ceneoProductExtractor.extractProductsFromAllPages(startUrl);
        if (products.isEmpty()) {
            log.warn("No data were found. Cancelling further processing");
            throw new WebScrapperException("No data found for this site");
        }

        return products;
    }
}

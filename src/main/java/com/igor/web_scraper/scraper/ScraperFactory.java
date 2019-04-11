package com.igor.web_scraper.scraper;

import com.igor.web_scraper.exception.WebScrapperException;
import com.igor.web_scraper.scraper.ceneo.*;

public class ScraperFactory {
    public Scraper<Product> getProductScraper(Site site) {
        switch (site) {
            case CENEO:
                SiteConnector siteConnector = new SiteConnector(new ConnectionBackoff());
                return new CeneoProductScraper(
                        new CeneoProductExtractor(
                                new CeneoSiteTraverser(siteConnector),
                                new CeneoProductMiner(new CeneoDefinitions(), siteConnector)));
            default:
                throw new WebScrapperException(String.format("Could not find scraper for type %s", site));
        }
    }
}

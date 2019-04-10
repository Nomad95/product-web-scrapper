package com.igor.web_scraper.exporter;

import com.igor.web_scraper.cli.CommandLineArguments;
import com.igor.web_scraper.parser.Parser;
import com.igor.web_scraper.parser.ParserFactory;
import com.igor.web_scraper.scraper.Product;
import com.igor.web_scraper.scraper.Scraper;
import com.igor.web_scraper.scraper.ScraperFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ProductExporter {

    private final ScraperFactory scraperFactory;
    private final ParserFactory parserFactory;

    public void export(CommandLineArguments arguments) {
        log.debug("Started exporting products");
        //get scraper
        Scraper<Product> scraper = scraperFactory.getProductScraper(arguments.getSite());
        //scrap web
        List<Product> products = scraper.scrapSite(arguments.getUrl());
        //get results
        //get parser
        Parser parser = parserFactory.getParser(arguments.getParserType());
        //parse to file
        //save file
        log.debug("Products exported successfully");
    }
}

package com.igor.web_scraper.exporter;

import com.igor.web_scraper.cli.CommandLineArguments;
import com.igor.web_scraper.exception.WebScrapperException;
import com.igor.web_scraper.output.FileSaver;
import com.igor.web_scraper.parser.Parser;
import com.igor.web_scraper.parser.ParserFactory;
import com.igor.web_scraper.scraper.Product;
import com.igor.web_scraper.scraper.Scraper;
import com.igor.web_scraper.scraper.ScraperFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

import static com.igor.web_scraper.WebScraper.notifyUser;
import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
public class ProductExporter {

    private final ScraperFactory scraperFactory;
    private final ParserFactory parserFactory;
    private final FileSaver fileSaver;

    public void export(CommandLineArguments arguments) {
        log.debug("Started exporting products");
        Scraper<Product> scraper = scraperFactory.getProductScraper(arguments.getSite());
        List<Product> products = scraper.scrapSite(arguments.getUrl());

        Parser parser = parserFactory.getParser(arguments.getParserType());
        byte[] output = parser.parse(products);

        trySaveFile(arguments, output);
        notifyUser(format("Successfully retrieved %d products", products.size()));
        log.debug("Products exported successfully");
    }

    private void trySaveFile(CommandLineArguments arguments, byte[] output) {
        try {
            fileSaver.saveToFile(output, arguments.getOutputDirectory(), arguments.getParserType().getExtension());
        } catch (IOException e) {
            log.error("Could not save file to filesystem!");
            throw new WebScrapperException(String.format("Could not save file: %s", e.getMessage()), e);
        }
    }
}

package com.igor.web_scraper.scraper.ceneo;

import com.igor.web_scraper.scraper.Product;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

import static com.igor.web_scraper.WebScraper.notifyUser;
import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
public class CeneoProductExtractor {

    private final CeneoSiteTraverser ceneoSiteTraverser;
    private final CeneoProductMiner ceneoProductMiner;

    public List<Product> extractProductsFromAllPages(@NonNull String startUrl) {
        log.debug("Started extracting from url {}", startUrl);
        notifyUser(format("Accessing %s...", startUrl));
        Document document = ceneoSiteTraverser.getDocument(startUrl);
        notifyUser(format("Pending for products from %s...", startUrl));
        List<Product> result = new ArrayList<>(ceneoProductMiner.getProductsFromSite(document));

        while (ceneoSiteTraverser.hasNextSite(document)) {
            String nextPage = ceneoSiteTraverser.getNextSiteUrl(document);
            log.debug("Started extracting from url {}", nextPage);
            notifyUser(format("Accessing %s...", nextPage));
            document = ceneoSiteTraverser.getDocument(nextPage);
            notifyUser(format("Pending for products from %s...", nextPage));
            result.addAll(ceneoProductMiner.getProductsFromSite(document));
        }

        log.debug("Products were extracted from all pages");
        return result;
    }
}

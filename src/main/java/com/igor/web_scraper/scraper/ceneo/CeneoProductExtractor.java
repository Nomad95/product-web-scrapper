package com.igor.web_scraper.scraper.ceneo;

import com.igor.web_scraper.scraper.Product;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CeneoProductExtractor {

    private final CeneoSiteTraverser ceneoSiteTraverser;
    private final CeneoProductMiner ceneoProductMiner;

    public List<Product> extractProductsFromAllPages(@NonNull String startUrl) {
        Document document = ceneoSiteTraverser.getDocument(startUrl);
        List<Product> result = new ArrayList<>(ceneoProductMiner.getProductsFromSite(document));

        while (ceneoSiteTraverser.hasNextSite(document)) {
            String nextPage = ceneoSiteTraverser.getNextSiteUrl(document);
            document = ceneoSiteTraverser.getDocument(nextPage);
            result.addAll(ceneoProductMiner.getProductsFromSite(document));
        }

        return result;
    }
}

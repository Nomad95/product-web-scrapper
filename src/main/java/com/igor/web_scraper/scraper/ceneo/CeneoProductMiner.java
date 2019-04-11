package com.igor.web_scraper.scraper.ceneo;

import com.igor.web_scraper.scraper.Product;
import com.igor.web_scraper.scraper.SiteConnector;
import com.igor.web_scraper.scraper.ceneo.CeneoDefinitions.ProductDefinition;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CeneoProductMiner {

    private final CeneoDefinitions ceneoDefinitions;
    private final SiteConnector siteConnector;
    private ProductDefinition currentDefinition;

    public List<Product> getProductsFromSite(Document html) {
        log.debug("Extracting products from {}", html.title());
        for (ProductDefinition definition : ceneoDefinitions.getDefinitions()) {
            currentDefinition = definition;
            List<Product> products = html.getElementsByClass(definition.getContainerElementsClass())
                    .stream()
                    .map(parseEachElementToProduct())
                    .collect(Collectors.toList());
            if (!products.isEmpty())
                return products;
        }

        return Collections.emptyList();
    }

    private Function<Element, Product> parseEachElementToProduct() {
        return element -> {
            Elements img = element.select(currentDefinition.getImageSelector());
            byte[] image = safeGetProductImage(img);
            String productName = element.select(currentDefinition.getNameSelector()).text();
            String productPrice = element.select(currentDefinition.getPriceSelector()).text();
            return new Product(productName, productPrice, image);
        };
    }

    private byte[] safeGetProductImage(Elements img) {
        String imageUrl = "";
        if (img.hasAttr(currentDefinition.getSubstituteImageAttribute())) {
            imageUrl = findImageUrl(img, imageUrl, currentDefinition.getSubstituteImageAttribute());
        }
        else if (img.hasAttr(currentDefinition.getMainImageAttribute())) {
            imageUrl = findImageUrl(img, imageUrl, currentDefinition.getMainImageAttribute());
        }
        else {
            log.debug("Image could not be found");
            return new byte[0];
        }
        return siteConnector.safeGetContentAsBytes(imageUrl);
    }

    private String findImageUrl(Elements img, String imageUrl, String substituteImageAttribute) {
        String absoluteUrl = img.first().absUrl(substituteImageAttribute);
        imageUrl += absoluteUrl.isEmpty() ? currentDefinition.getProtocol() + img.first().attr(substituteImageAttribute) : absoluteUrl;
        return imageUrl;
    }
}
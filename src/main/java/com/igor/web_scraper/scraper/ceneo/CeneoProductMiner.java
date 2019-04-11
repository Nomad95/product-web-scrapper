package com.igor.web_scraper.scraper.ceneo;

import com.igor.web_scraper.scraper.Product;
import com.igor.web_scraper.scraper.SiteConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
public class CeneoProductMiner {

    private static final String IMAGE_SELECTOR = "div.category-item-box-picture > a > img";
    private static final String NAME_SELECTOR = "strong > a";
    private static final String PRICE_SELECTOR = "div.category-item-box-price > a > span > span.price-format.nowrap > span";
    private static final String SUBSTITUTE_IMAGE_ATTRIBUTE = "data-original";
    private static final String MAIN_IMAGE_ATTRIBUTE = "src";

    private final SiteConnector siteConnector;

    public List<Product> getProductsFromSite(Document html) {
        log.debug("Extracting products from {}", html.title());
        return html.getElementsByClass("category-item-box")
                .stream()
                .map(parseEachElementToProduct())
                .collect(Collectors.toList());
    }

    private Function<Element, Product> parseEachElementToProduct() {
        return element -> {
            Elements img = element.select(IMAGE_SELECTOR);
            byte[] image = safeGetProductImage(img);
            String productName = element.select(NAME_SELECTOR).text();
            String productPrice = element.select(PRICE_SELECTOR).text();
            return new Product(productName, productPrice, image);
        };
    }

    private byte[] safeGetProductImage(Elements img) {
        String imageUrl = "";
        if (img.hasAttr(SUBSTITUTE_IMAGE_ATTRIBUTE))
            imageUrl += img.first().absUrl(SUBSTITUTE_IMAGE_ATTRIBUTE);
        else if (img.hasAttr(MAIN_IMAGE_ATTRIBUTE))
            imageUrl += img.first().absUrl(MAIN_IMAGE_ATTRIBUTE);
        else {
            log.debug("Image could not be found");
            return new byte[0];
        }
        return siteConnector.safeGetContentAsBytes(imageUrl);
    }
}
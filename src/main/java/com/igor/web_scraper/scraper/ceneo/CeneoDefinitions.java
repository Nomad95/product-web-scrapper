package com.igor.web_scraper.scraper.ceneo;

import lombok.Value;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CeneoDefinitions {

    private List<ProductDefinition> definitions = new ArrayList<>(2);

    public CeneoDefinitions() {
        definitions.add(new ProductDefinition(
                "div.cat-prod-row-foto > a > img",
                "div.cat-prod-row-content > div.cat-prod-row-desc > strong > a",
                "div.cat-prod-row-content > div.cat-prod-row-price > a:nth-child(1) > span.price-format.nowrap > span",
                "data-original",
                "src",
                "cat-prod-row",
                "https:"
        ));
        definitions.add(new ProductDefinition(
                "div.category-item-box-picture > a > img",
                "strong > a",
                "div.category-item-box-price > a > span > span.price-format.nowrap > span",
                "data-original",
                "src",
                "category-item-box",
                "category-item-box"
        ));
    }

    public List<ProductDefinition> getDefinitions() {
        return Collections.unmodifiableList(definitions);
    }

    @Value
    public static class ProductDefinition {
        String imageSelector;
        String nameSelector;
        String priceSelector;
        String substituteImageAttribute;
        String mainImageAttribute;
        String containerElementsClass;
        String protocol;
    }
}

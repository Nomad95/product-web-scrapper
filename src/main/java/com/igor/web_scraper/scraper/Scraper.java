package com.igor.web_scraper.scraper;

import java.util.List;

public interface Scraper<T> {

    List<T> scrapSite(String url);
}

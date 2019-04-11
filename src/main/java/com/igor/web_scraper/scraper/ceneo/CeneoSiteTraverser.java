package com.igor.web_scraper.scraper.ceneo;

import com.igor.web_scraper.scraper.SiteConnector;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;

import static com.igor.web_scraper.scraper.ceneo.CeneoDefinitions.NEXT_PAGE_CENEO_BUTTON;

@Slf4j
@RequiredArgsConstructor
public class CeneoSiteTraverser {

    private static final String HOST = "www.ceneo.pl";
    private static final String BASE_URL = "https://www.ceneo.pl";

    private final SiteConnector siteConnector;

    public Document getDocument(@NonNull String url) {
        log.debug("Creating request to {}", url);
        Connection connection = siteConnector.getNewConnection(url);
        siteConnector.setHost(connection, HOST);
        siteConnector.addStandardDisguiseHeaders(connection);
        return siteConnector.getDocument(connection);
    }

    public boolean hasNextSite(@NonNull Document html) {
        return !html.getElementsByClass(NEXT_PAGE_CENEO_BUTTON).isEmpty();
    }

    public String getNextSiteUrl(@NonNull Document html) {
        String relativeUrl = html.getElementsByClass(NEXT_PAGE_CENEO_BUTTON)
                .first()
                .children()
                .first()
                .attr("href");

        return BASE_URL + relativeUrl;
    }
}

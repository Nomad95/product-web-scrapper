package unit.scraper

import com.igor.web_scraper.exception.WebScrapperException
import com.igor.web_scraper.scraper.Product
import com.igor.web_scraper.scraper.ceneo.CeneoProductExtractor
import com.igor.web_scraper.scraper.ceneo.CeneoProductScraper
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import spock.lang.Specification

class CeneoProductScraperUnitTest extends Specification {

    def URL = "https://www.ceneo.pl/Gitary/p:Yamaha_Music/Rodzaj:Elektryczne.htm"
    CeneoProductScraper ceneoProductScraper
    CeneoProductExtractor ceneoDataExtractor
    Connection fakeConnection
    Document fakeDocument

    def setup() {
        ceneoDataExtractor = Stub()
        fakeConnection = Jsoup.connect("https://ceneo.pl")
        fakeDocument = new Document("https://ceneo.pl")
        ceneoProductScraper = new CeneoProductScraper(ceneoDataExtractor)
    }

    def "scraper should throw when no data were fetched"() {
        given: "document extractor returns empty list"
        ceneoDataExtractor.extractProductsFromAllPages(URL) >> []

        when:
        ceneoProductScraper.scrapSite(URL)

        then:
        def exception = thrown(WebScrapperException)
        exception.message == "No data found for this site"
    }

    def "scraper should retrieve product list when traversed successfully"() {
        given: "successful connection and extracted fake products"
        ceneoDataExtractor.extractProductsFromAllPages(URL) >> [new Product("", "", new byte[0])]

        when:
        List<Product> products = ceneoProductScraper.scrapSite(URL)

        then: "product list is returned"
        !products.isEmpty()
    }
}

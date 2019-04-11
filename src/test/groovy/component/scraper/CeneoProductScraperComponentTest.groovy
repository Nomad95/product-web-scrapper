package component.scraper

import com.igor.web_scraper.scraper.Product
import com.igor.web_scraper.scraper.SiteConnector
import com.igor.web_scraper.scraper.ceneo.*
import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import spock.lang.Specification

class CeneoProductScraperComponentTest extends Specification {

    def URL = "https://www.ceneo.pl/Gitary/p:Yamaha_Music/Rodzaj:Elektryczne.htm"
    SiteConnector siteConnector
    CeneoProductScraper ceneoProductScraper
    byte[] fakeBytes = new byte[0]
    Document firstSite = Jsoup.parse(IOUtils.toString(getClass().getResourceAsStream("/html/ceneo.html")))
    Document secondSite = Jsoup.parse(IOUtils.toString(getClass().getResourceAsStream("/html/ceneo-last.html")))

    def setup() {
        siteConnector = Stub()
        ceneoProductScraper =  new CeneoProductScraper(
                new CeneoProductExtractor(
                        new CeneoSiteTraverser(siteConnector),
                        new CeneoProductMiner(new CeneoDefinitions(), siteConnector)))

    }

    def "should parse all pages and return all products from two sites"() {
        given: "Stubbed endpoint connection"
        siteConnector.getDocument(_) >>> [firstSite, secondSite]
        siteConnector.safeGetContentAsBytes(_) >> fakeBytes

        when:
        List<Product> products = ceneoProductScraper.scrapSite(URL)

        then:
        products.size() == 56
    }

    def "returned product should contain proper product definitions"() {
        given: "Stubbed endpoint connection"
        Product productFromFirstSite = new Product("Yamaha RGX121z", "829,00", fakeBytes)
        Product productFromSecondSite = new Product("Yamaha Pacifica 311H Black", "1346,40", fakeBytes)
        siteConnector.getDocument(_) >>> [firstSite, secondSite]
        siteConnector.safeGetContentAsBytes(_) >> fakeBytes

        when:
        List<Product> products = ceneoProductScraper.scrapSite(URL)

        then:
        products.containsAll([productFromFirstSite, productFromSecondSite])
    }
}

package unit.scraper

import com.igor.web_scraper.scraper.Product
import com.igor.web_scraper.scraper.ceneo.CeneoProductExtractor
import com.igor.web_scraper.scraper.ceneo.CeneoProductMiner
import com.igor.web_scraper.scraper.ceneo.CeneoSiteTraverser
import org.jsoup.nodes.Document
import spock.lang.Specification

class CeneoProductExtractorUnitTest extends Specification {

    def URL = "https://www.ceneo.pl/Gitary/p:Yamaha_Music/Rodzaj:Elektryczne.htm"
    CeneoSiteTraverser ceneoSiteTraverser
    CeneoProductExtractor ceneoDataExtractor
    CeneoProductMiner ceneoProductMiner
    Document document
    Product prod1
    Product prod2
    Product prod3

    def setup() {
        document = Stub()
        ceneoSiteTraverser = Mock()
        ceneoProductMiner = Mock()
        ceneoDataExtractor = new CeneoProductExtractor(ceneoSiteTraverser, ceneoProductMiner)
        prod1 = new Product("", "", new byte[0])
        prod2 = new Product("", "", new byte[0])
        prod3 = new Product("", "", new byte[0])
    }

    def "should get products from one page"() {
        given:
        ceneoSiteTraverser.getDocument(URL) >> document
        ceneoProductMiner.getProductsFromSite(document) >> [prod1, prod2, prod3]
        ceneoSiteTraverser.hasNextSite(document) >> false

        when:
        List<Product> productsFromAllPages = ceneoDataExtractor.extractProductsFromAllPages(URL)

        then:
        productsFromAllPages.containsAll([prod1, prod2, prod3])
    }

    def "should get all products from many sites"() {
        given:
        ceneoSiteTraverser.getDocument(URL) >> document
        ceneoProductMiner.getProductsFromSite(document) >>> [[prod1], [prod2], [prod3]]
        ceneoSiteTraverser.hasNextSite(document) >>> [true, true, false]
        ceneoSiteTraverser.getNextSiteUrl(document) >> URL

        when:
        List<Product> productsFromAllPages = ceneoDataExtractor.extractProductsFromAllPages(URL)

        then:
        productsFromAllPages.containsAll([prod1, prod2, prod3])
    }

    def "should return empty list when no elements found"() {
        given:
        ceneoSiteTraverser.getDocument(URL) >> document
        ceneoProductMiner.getProductsFromSite(document) >> []
        ceneoSiteTraverser.hasNextSite(document) >> false

        when:
        List<Product> productsFromAllPages = ceneoDataExtractor.extractProductsFromAllPages(URL)

        then:
        productsFromAllPages.isEmpty()
    }

    def "should throw NPE when passed null url"() {
        when:
        ceneoDataExtractor.extractProductsFromAllPages(null)

        then:
        thrown(NullPointerException)
    }
}

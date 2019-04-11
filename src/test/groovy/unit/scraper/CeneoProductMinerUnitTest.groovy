package unit.scraper

import com.igor.web_scraper.scraper.Product
import com.igor.web_scraper.scraper.SiteConnector
import com.igor.web_scraper.scraper.ceneo.CeneoDefinitions
import com.igor.web_scraper.scraper.ceneo.CeneoProductMiner
import org.apache.commons.io.IOUtils
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import spock.lang.Specification

class CeneoProductMinerUnitTest extends Specification {

    CeneoProductMiner ceneoProductMiner
    SiteConnector siteConnector
    byte[] fakeBytes = new byte[0]
    Product product1 = new Product("Yamaha Pacifica 112V BL", "999,00", fakeBytes)
    Product product2 = new Product("Yamaha RGX121z", "829,00", fakeBytes)
    Product product3 = new Product("Yamaha Pacifica PAC 611 HFM RB", "2499,00", fakeBytes)
    Product product4 = new Product("Aquaman [Blu-Ray 4K]+[Blu-Ray]", "124,99", fakeBytes)
    Product product5 = new Product("Matrix Reaktywacja (Premium Collection) (Blu-ray)", "56,02", fakeBytes)
    Document documentType1 = Jsoup.parse(IOUtils.toString(getClass().getResourceAsStream("/html/ceneo.html")))
    Document documentType2 = Jsoup.parse(IOUtils.toString(getClass().getResourceAsStream("/html/ceneo-type2.html")))

    def setup() {
        siteConnector = Stub()
        ceneoProductMiner = new CeneoProductMiner(new CeneoDefinitions(), siteConnector)
    }

    def "should get products from document"() {
        given:
        siteConnector.safeGetContentAsBytes(_) >> fakeBytes

        when:
        List<Product> products = ceneoProductMiner.getProductsFromSite(documentType1)

        then:
        products.containsAll([product1, product2, product3])
    }

    def "should get products from document with another site layout"() {
        given:
        siteConnector.safeGetContentAsBytes(_) >> fakeBytes

        when:
        List<Product> products = ceneoProductMiner.getProductsFromSite(documentType2)

        then:
        products.containsAll([product4, product5])
    }

    def "should return empty byte array when no image was found"() {
        given: "modified htm with no proper image attributes"
        Product expectedProduct = new Product("Yamaha Pacifica 112V BL", "999,00", new byte[0])
        documentType1 = Jsoup.parse(IOUtils.toString(getClass().getResourceAsStream("/html/ceneo.html"))
                .replace("data-original", "abc")
                .replace("<img src", "<img sc"))
        siteConnector.safeGetContentAsBytes(_) >> fakeBytes

        when:
        List<Product> products = ceneoProductMiner.getProductsFromSite(documentType1)

        then:
        noExceptionThrown()
        products.contains(expectedProduct)
    }
}

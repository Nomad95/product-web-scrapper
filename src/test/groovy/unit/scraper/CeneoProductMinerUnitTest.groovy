package unit.scraper

import com.igor.web_scraper.scraper.Product
import com.igor.web_scraper.scraper.SiteConnector
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
    Document document = Jsoup.parse(IOUtils.toString(getClass().getResourceAsStream("/html/ceneo.html")))

    def setup() {
        siteConnector = Stub()
        ceneoProductMiner = new CeneoProductMiner(siteConnector)
    }

    def "should get products from document"() {
        given:
        siteConnector.safeGetContentAsBytes(_) >> fakeBytes

        when:
        List<Product> products = ceneoProductMiner.getProductsFromSite(document)

        then:
        products.containsAll([product1, product2, product3])
    }

//    //image.ceneostatic.pl/data/products/5743342/f-yamaha-pacifica-112v-bl.jpg
//    Yamaha Pacifica 112V BL
//    999,00
//    //image.ceneostatic.pl/data/products/13258017/f-yamaha-rgx121z.jpg
//    Yamaha RGX121z
//    829,00
//    //image.ceneostatic.pl/data/products/13431369/f-yamaha-pacifica-pac-611-hfm-rb.jpg
//    Yamaha Pacifica PAC 611 HFM RB
//    2499,00
}

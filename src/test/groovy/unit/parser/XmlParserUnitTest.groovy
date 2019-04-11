package unit.parser

import com.igor.web_scraper.parser.xml.XmlParser
import com.igor.web_scraper.scraper.Product
import spock.lang.Specification

class XmlParserUnitTest extends Specification {

    XmlParser xmlParser

    def setup() {
        xmlParser = new XmlParser()
    }

    def "should parse products to xml string"() {
        given:
        List<Product> products = [new Product("name", "price", new byte[0])]

        when:
        byte[] resultXml = xmlParser.parse(products)

        then:
        resultXml != null
        resultXml.length > 0
    }

}

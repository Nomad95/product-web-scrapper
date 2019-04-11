package unit.product_exporter

import com.igor.web_scraper.cli.ArgumentValidator
import com.igor.web_scraper.cli.CommandLineArguments
import com.igor.web_scraper.exporter.ProductExporter
import com.igor.web_scraper.parser.ParserFactory
import com.igor.web_scraper.parser.ParserType
import com.igor.web_scraper.parser.xml.ToXmlParser
import com.igor.web_scraper.scraper.ScraperFactory
import com.igor.web_scraper.scraper.Site
import com.igor.web_scraper.scraper.ceneo.CeneoProductScraper
import spock.lang.Specification

class ProductExporterUnitTest extends Specification {

    ScraperFactory scraperFactory
    ParserFactory parserFactory
    ProductExporter productExporter
    CeneoProductScraper ceneoProductScraper
    ToXmlParser xmlParser
    CommandLineArguments cla

    def setup() {
        scraperFactory = Mock()
        parserFactory = Mock()
        ceneoProductScraper = Mock()
        xmlParser = Mock()
        productExporter = new ProductExporter(scraperFactory, parserFactory)
        cla = new CommandLineArguments(new ArgumentValidator())
    }

    def "should find scraper"() {
        given:
        ceneoProductScraper.scrapSite(_) >> []
        cla.site = Site.CENEO

        when:
        productExporter.export(cla)

        then:
        1 * scraperFactory.getProductScraper(Site.CENEO) >> ceneoProductScraper
    }

    def "should find parser"() {
        given:
        ceneoProductScraper.scrapSite(_) >> []
        scraperFactory.getProductScraper(Site.CENEO) >> ceneoProductScraper
        cla.parserType = ParserType.XML

        when:
        productExporter.export(cla)

        then:
        1 * parserFactory.getParser(ParserType.XML) >> xmlParser
    }
}

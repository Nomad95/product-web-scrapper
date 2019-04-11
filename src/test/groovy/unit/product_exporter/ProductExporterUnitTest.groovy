package unit.product_exporter

import com.igor.web_scraper.cli.ArgumentValidator
import com.igor.web_scraper.cli.CommandLineArguments
import com.igor.web_scraper.exception.WebScrapperException
import com.igor.web_scraper.exporter.ProductExporter
import com.igor.web_scraper.output.FileSaver
import com.igor.web_scraper.parser.ParserFactory
import com.igor.web_scraper.parser.ParserType
import com.igor.web_scraper.parser.xml.XmlParser
import com.igor.web_scraper.scraper.Product
import com.igor.web_scraper.scraper.ScraperFactory
import com.igor.web_scraper.scraper.Site
import com.igor.web_scraper.scraper.ceneo.CeneoProductScraper
import spock.lang.Specification

class ProductExporterUnitTest extends Specification {

    ScraperFactory scraperFactory
    ParserFactory parserFactory
    ProductExporter productExporter
    CeneoProductScraper ceneoProductScraper
    FileSaver fileSaver
    XmlParser xmlParser
    CommandLineArguments cla

    def exampleBytes = new byte[0]
    def exampleDirectory = new File("/path")
    def exampleProduct = new Product("name", "price", "<img>".bytes)

    def setup() {
        scraperFactory = Mock()
        parserFactory = Mock()
        ceneoProductScraper = Mock()
        fileSaver = Mock()
        xmlParser = Mock()
        productExporter = new ProductExporter(scraperFactory, parserFactory, fileSaver)
        cla = new CommandLineArguments(new ArgumentValidator())
    }

    def "should find scraper"() {
        given:
        ceneoProductScraper.scrapSite(_) >> []
        xmlParser.parse([exampleProduct]) >> exampleBytes
        parserFactory.getParser(ParserType.XML) >> xmlParser
        xmlParser.parse([exampleProduct]) >> exampleBytes
        cla.site = Site.CENEO

        when:
        productExporter.export(cla)

        then:
        1 * scraperFactory.getProductScraper(Site.CENEO) >> ceneoProductScraper
    }

    def "should scrap site"() {
        given:
        Product exampleProduct = exampleProduct
        scraperFactory.getProductScraper(Site.CENEO) >> ceneoProductScraper
        parserFactory.getParser(ParserType.XML) >> xmlParser
        xmlParser.parse([exampleProduct]) >> exampleBytes
        cla.outputDirectory = exampleDirectory

        when:
        productExporter.export(cla)

        then:
        1 * ceneoProductScraper.scrapSite(_) >> [exampleProduct]

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

    def "should parse products when found"() {
        given:
        Product exampleProduct = exampleProduct
        ceneoProductScraper.scrapSite(_) >> [exampleProduct]
        scraperFactory.getProductScraper(Site.CENEO) >> ceneoProductScraper
        parserFactory.getParser(ParserType.XML) >> xmlParser
        cla.outputDirectory = exampleDirectory

        when:
        productExporter.export(cla)

        then:
        1 * xmlParser.parse([exampleProduct]) >> exampleBytes
    }

    def "should save output file when successfully obtained the products"() {
        given:
        Product exampleProduct = exampleProduct
        ceneoProductScraper.scrapSite(_) >> [exampleProduct]
        xmlParser.parse([exampleProduct]) >> exampleBytes
        scraperFactory.getProductScraper(Site.CENEO) >> ceneoProductScraper
        parserFactory.getParser(ParserType.XML) >> xmlParser
        cla.outputDirectory = exampleDirectory

        when:
        productExporter.export(cla)

        then:
        1 * fileSaver.saveToFile(exampleBytes, cla.outputDirectory, ParserType.XML.getExtension())
    }

    def "should throw WebScraperException when file could not be saved"() {
        given:
        Product exampleProduct = exampleProduct
        ceneoProductScraper.scrapSite(_) >> [exampleProduct]
        xmlParser.parse([exampleProduct]) >> exampleBytes
        scraperFactory.getProductScraper(Site.CENEO) >> ceneoProductScraper
        parserFactory.getParser(ParserType.XML) >> xmlParser
        fileSaver.saveToFile(_, _, _) >> { throw new IOException("error!") }
        cla.outputDirectory = exampleDirectory

        when:
        productExporter.export(cla)

        then:
        def exception = thrown(WebScrapperException)
        exception.getMessage().contains("Could not save file")
    }
}

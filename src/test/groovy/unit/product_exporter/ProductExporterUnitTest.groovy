package unit.product_exporter

import com.igor.web_scraper.cli.ArgumentValidator
import com.igor.web_scraper.cli.CommandLineArguments
import com.igor.web_scraper.exception.WebScrapperException
import com.igor.web_scraper.exporter.ProductExporter
import com.igor.web_scraper.output.FileSaver
import com.igor.web_scraper.output.PathCreator
import com.igor.web_scraper.parser.ParserFactory
import com.igor.web_scraper.parser.ParserType
import com.igor.web_scraper.parser.xml.XmlParser
import com.igor.web_scraper.scraper.Product
import com.igor.web_scraper.scraper.ScraperFactory
import com.igor.web_scraper.scraper.Site
import com.igor.web_scraper.scraper.ceneo.CeneoProductScraper
import spock.lang.Specification

class ProductExporterUnitTest extends Specification {

    CeneoProductScraper ceneoProductScraper
    ProductExporter productExporter
    ScraperFactory scraperFactory
    ParserFactory parserFactory
    CommandLineArguments cla
    PathCreator pathCreator
    FileSaver fileSaver
    XmlParser xmlParser

    def exampleBytes = new byte[0]
    def exampleDirectory = new File("/path")
    def exampleProduct = new Product("name", "price", "<img>".bytes)

    def setup() {
        scraperFactory = Mock()
        parserFactory = Mock()
        ceneoProductScraper = Mock()
        pathCreator = Mock()
        fileSaver = Mock()
        xmlParser = Mock()
        productExporter = new ProductExporter(scraperFactory, parserFactory, fileSaver, pathCreator)
        cla = new CommandLineArguments(new ArgumentValidator())

        pathCreator.createTargetFilePath(_) >> "."
        scraperFactory.getProductScraper(Site.CENEO) >> ceneoProductScraper
        parserFactory.getParser(ParserType.XML) >> xmlParser
        xmlParser.parse([exampleProduct]) >> exampleBytes
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

    def "should scrap site"() {
        given:
        cla.outputDirectory = exampleDirectory

        when:
        productExporter.export(cla)

        then:
        1 * ceneoProductScraper.scrapSite(_) >> [exampleProduct]

    }

    def "should find parser"() {
        given:
        ceneoProductScraper.scrapSite(_) >> []
        cla.parserType = ParserType.XML

        when:
        productExporter.export(cla)

        then:
        1 * parserFactory.getParser(ParserType.XML) >> xmlParser
    }

    def "should parse products when found"() {
        given:
        ceneoProductScraper.scrapSite(_) >> [exampleProduct]
        cla.outputDirectory = exampleDirectory

        when:
        productExporter.export(cla)

        then:
        1 * xmlParser.parse([exampleProduct]) >> exampleBytes
    }

    def "should save output file when successfully obtained the products"() {
        given:
        ceneoProductScraper.scrapSite(_) >> [exampleProduct]
        cla.outputDirectory = exampleDirectory

        when:
        productExporter.export(cla)

        then:
        1 * fileSaver.saveToFile(exampleBytes, _ as File)
    }

    def "should throw WebScraperException when file could not be saved"() {
        given:
        ceneoProductScraper.scrapSite(_) >> [exampleProduct]
        fileSaver.saveToFile(_ as byte[], _ as File) >> { throw new IOException("error!") }
        cla.outputDirectory = exampleDirectory

        when:
        productExporter.export(cla)

        then:
        def exception = thrown(WebScrapperException)
        exception.getMessage().contains("Could not save file")
    }
}

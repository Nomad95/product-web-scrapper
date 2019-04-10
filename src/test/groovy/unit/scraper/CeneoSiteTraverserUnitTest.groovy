package unit.scraper

import com.igor.web_scraper.scraper.SiteConnector
import com.igor.web_scraper.scraper.ceneo.CeneoSiteTraverser
import org.apache.commons.io.IOUtils
import org.jsoup.Connection
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import spock.lang.Specification

class CeneoSiteTraverserUnitTest extends Specification {

    def URL = "https://www.ceneo.pl/Gitary/p:Yamaha_Music/Rodzaj:Elektryczne.htm"
    CeneoSiteTraverser ceneoSiteTraverser
    SiteConnector siteConnector
    Document document
    Document lastPage
    Connection connection

    def setup() {
        document = Stub()
        siteConnector = Stub()
        ceneoSiteTraverser = new CeneoSiteTraverser(siteConnector)
        document = Jsoup.parse(IOUtils.toString(getClass().getResourceAsStream("/html/ceneo.html")))
        lastPage = Jsoup.parse(IOUtils.toString(getClass().getResourceAsStream("/html/ceneo-last.html")))
    }

    def "should get html from url" () {
        given:
        siteConnector.getNewConnection(URL) >> connection
        siteConnector.getDocument(connection) >> document

        when:
        Document result = ceneoSiteTraverser.getDocument(URL)

        then:
        result == document
    }

    def "should find next page url" () {
        when:
        def url = ceneoSiteTraverser.getNextSiteUrl(document)

        then:
        url == "https://www.ceneo.pl/Gitary/p:Yamaha_Music/Rodzaj:Elektryczne;0020-30-0-0-1.htm"
    }

    def "should check if next page exists" () {
        expect:
        ceneoSiteTraverser.hasNextSite(document)
    }

    def "should verify that its last page" () {
        expect:
        !ceneoSiteTraverser.hasNextSite(lastPage)
    }
}

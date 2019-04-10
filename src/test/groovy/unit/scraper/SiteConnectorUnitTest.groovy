package unit.scraper

import com.igor.web_scraper.exception.WebScrapperException
import com.igor.web_scraper.scraper.ConnectionBackoff
import com.igor.web_scraper.scraper.SiteConnector
import org.jsoup.Connection
import org.jsoup.HttpStatusException
import org.jsoup.nodes.Document
import spock.lang.Specification

class SiteConnectorUnitTest extends Specification {

    def DEFAULT_URL = "http://ceneo.pl"
    def DEFAULT_MESSAGE = "msg"
    Document document
    Connection connection
    SiteConnector siteConnector
    ConnectionBackoff connectionBackoff

    def setup() {
        connection = Stub()
        connectionBackoff = Mock()
        document = Stub()
        siteConnector = new SiteConnector(connectionBackoff)
    }

    def "should throw WebScraperException when site was not found at provided url"() {
        given:
        connection.get() >> { throw new HttpStatusException(DEFAULT_MESSAGE, 404, DEFAULT_URL) }

        when:
        siteConnector.getDocument(connection)

        then:
        def exception = thrown(WebScrapperException)
        exception.message == "Site " + DEFAULT_URL + " was not found"
    }

    def "should throw WebScraperException when there was an server error"() {
        given:
        connection.get() >> { throw new HttpStatusException(DEFAULT_MESSAGE, 500, DEFAULT_URL) }

        when:
        siteConnector.getDocument(connection)

        then:
        def exception = thrown(WebScrapperException)
        exception.message == "Server responded with HTTP 500 at url: " + DEFAULT_URL
    }

    def "should try to reconnect when timeout"() { //TODO: component test this
        given:
        connection.get() >> { throw new SocketTimeoutException(DEFAULT_URL) } >> this.document

        when:
        siteConnector.getDocument(connection)

        then:
        noExceptionThrown()
    }

    def "should throw WebScraperException when no connection"() {
        given:
        connection.get() >> { throw new UnknownHostException(DEFAULT_URL) }

        when:
        siteConnector.getDocument(connection)

        then:
        def exception = thrown(WebScrapperException)
        exception.message == "Could not connect to host. Check your internet connection"
    }

    def "should throw WebScraperException when other IOException occurs"() {
        given:
        connection.get() >> { throw new IOException(DEFAULT_URL) }

        when:
        siteConnector.getDocument(connection)

        then:
        def exception = thrown(WebScrapperException)
        exception.message == "Could not connect to host"
    }
}

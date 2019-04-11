package unit.scraper

import com.igor.web_scraper.exception.WebScrapperException
import com.igor.web_scraper.scraper.ConnectionBackoff
import spock.lang.Specification

class ConnectionBackoffUnitTest extends Specification {

    def "should throw when retry threshold has exceed its limit"() {
        when:
        new ConnectionBackoff().tryRetryConnection(0, null)

        then:
        def exception = thrown(WebScrapperException)
        exception.message == "Could not connect to host"
    }

    def "should wait 1 second before next request"() {
        when:
        def before = System.currentTimeMillis()
        new ConnectionBackoff().tryRetryConnection(1, null)
        def after = System.currentTimeMillis()

        then:
        after - before >= 1000
        println after - before
    }
}

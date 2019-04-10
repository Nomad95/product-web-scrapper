package unit.cli

import com.igor.web_scraper.cli.ArgumentValidator
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification

class ArgumentValidatorUnitTest extends Specification {

    @Rule
    TemporaryFolder folder = new TemporaryFolder()

    def "should return true when url is valid"() {
        expect:
        new ArgumentValidator().isUrlValid("http://valid.url.pl")
    }

    def "should return false when url is invalid"() {
        expect:
        !new ArgumentValidator().isUrlValid("something://invalid")
    }

    def "should throw when passed null url"() {
        when:
        new ArgumentValidator().isUrlValid(null)

        then:
        thrown(NullPointerException)
    }

    def "should return false when passed a file"() {
        expect:
        !new ArgumentValidator().isAValidDirectory(folder.newFile())
    }

    def "should return true when passed a directory"() {
        expect:
        new ArgumentValidator().isAValidDirectory(folder.newFolder())
    }

    def "should throw when passed null file"() {
        when:
        new ArgumentValidator().isAValidDirectory(null)

        then:
        thrown(NullPointerException)
    }
}

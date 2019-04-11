package unit.output

import com.igor.web_scraper.output.FileSaver
import spock.lang.Specification

class FileSaverUnitTest extends Specification {

    File fakeDirectory
    FileSaver fileSaver

    def setup() {
        fakeDirectory = Mock()
        fileSaver = new FileSaver()
    }

    def "should try create new file path"() {
        when:
        fileSaver.saveToFile(new byte[0], fakeDirectory, ".xml")

        then:
        1 * fakeDirectory.getPath() >> ""
    }

    def "should not allow to pass null file data"() {
        when:
        fileSaver.saveToFile(null, fakeDirectory, ".xml")

        then:
        thrown(NullPointerException)
    }

    def "should not allow to pass null destination folder"() {
        when:
        fileSaver.saveToFile(new byte[0], null, ".xml")

        then:
        thrown(NullPointerException)
    }
}

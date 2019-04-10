package unit

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import spock.lang.Specification

class JsoupTest extends Specification {

    def "asdas"() {
        expect:
        Document doc = Jsoup.connect("https://www.ceneo.pl/Gitary/p:Yamaha_Music/Rodzaj:Elektryczne.htm").get()
        Elements element = doc.getElementsByClass("page-arrow arrow-next")
        String href = element.first().child(0).attr("href")
        println doc.body()
    }
}

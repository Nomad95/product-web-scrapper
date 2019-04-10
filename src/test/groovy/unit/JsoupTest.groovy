package unit

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import spock.lang.Specification

import java.util.stream.IntStream

//doc.getElementsByClass("category-item-box").get(0).getElementsByClass("category-item-box-picture").first().children().first().children().attr("src");
class JsoupTest extends Specification {

    def "asdas"() {
        expect:
        Document doc = Jsoup.connect("https://www.ceneo.pl/Gitary/p:Yamaha_Music/Rodzaj:Elektryczne.htm").get()
        Elements element = doc.getElementsByClass("page-arrow arrow-next")
        String href = element.first().child(0).attr("href")
        Elements guitars = doc.getElementsByClass("category-item-box")
        def iterator = guitars.iterator()
        int o = 1
        while (iterator.hasNext()) {
            println o + ' '
            o++
            def nextElement = iterator.next()
            Elements img = nextElement.select('div.category-item-box-picture > a > img')
            String url = ''
            if (img.hasAttr("data-original")) {
                url = img.first().absUrl("data-original")
            } else if (img.hasAttr("src")) {
                url = img.first().absUrl("src")
            }
            println nextElement.select('strong > a').text()
            println nextElement.select('div.category-item-box-price > a > span > span.price-format.nowrap > span').text()

            byte[] bytes = Jsoup.connect(url).ignoreContentType(true).execute().bodyAsBytes()
            println bytes
        }
        println doc.body()
    }

    def "albert"() {
        expect:
        def collect = IntStream.range(1, 1001).filter({ i -> String.valueOf(i).contains("9") }).toArray()
        println 'asd'
    }
}
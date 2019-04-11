package com.igor.web_scraper.parser.xml;

import com.igor.web_scraper.parser.Parser;
import com.igor.web_scraper.scraper.Product;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.util.List;

public class XmlParser implements Parser {

    private static final String XML_VERSION_TAG = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n";

    @Override
    public <T> byte[] parse(List<T> entries) {//TODO: definitions
        XStream xStream = new XStream(new DomDriver());
        xStream.alias("product", Product.class);

        return (XML_VERSION_TAG + xStream.toXML(entries)).getBytes();
    }
}

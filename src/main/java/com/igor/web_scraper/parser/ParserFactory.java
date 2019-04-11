package com.igor.web_scraper.parser;

import com.igor.web_scraper.exception.WebScrapperException;
import com.igor.web_scraper.parser.xml.XmlParser;

public class ParserFactory {

    public Parser getParser(ParserType parserType) {
        switch (parserType) {
            case XML:
                return new XmlParser();
            default:
                throw new WebScrapperException(String.format("Could not find parser for type %s", parserType));
        }
    }
}

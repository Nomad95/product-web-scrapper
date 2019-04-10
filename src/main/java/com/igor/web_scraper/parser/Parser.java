package com.igor.web_scraper.parser;

import java.util.List;

public interface Parser{

    <T> byte[] parse(List<T> entries);
}

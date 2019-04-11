package com.igor.web_scraper.parser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ParserType {
    XML(".xml");

    @Getter
    private final String extension;
}

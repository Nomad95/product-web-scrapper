package com.igor.web_scraper.output;

import com.igor.web_scraper.cli.CommandLineArguments;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

public class PathCreator {

    private static final String FOLDER_DATE_FORMATTER = "yyyy-MM-dd_HH-mm-ss";

    public String createTargetFilePath(CommandLineArguments arguments) {
        return arguments.getOutputDirectory().getPath()
                .concat(ZonedDateTime.now(ZoneId.of("Europe/Warsaw"))
                        .toLocalDateTime()
                        .format(ofPattern(FOLDER_DATE_FORMATTER)))
                .concat("/products")
                .concat(arguments.getParserType().getExtension());
    }
}

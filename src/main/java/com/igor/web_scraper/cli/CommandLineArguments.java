package com.igor.web_scraper.cli;

import com.igor.web_scraper.exception.WebScrapperException;
import com.igor.web_scraper.parser.ParserType;
import com.igor.web_scraper.scraper.Site;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;

import static com.igor.web_scraper.parser.ParserType.XML;
import static com.igor.web_scraper.scraper.Site.CENEO;
import static java.util.Objects.isNull;


@ToString
@RequiredArgsConstructor
@Command(version = "Product web scraper - 1.0.0 by Igor Kopeć")
public class CommandLineArguments implements Runnable {

    private final ArgumentValidator argumentValidator;

    @Getter
    @Option(names = { "-h", "--help" }, usageHelp = true, description = "Display a help message")
    private boolean helpRequested;

    @Getter
    @Option(names = {"-v", "--version"}, versionHelp = true, description = "Display version info")
    private boolean versionInfoRequested;

    @Getter
    @Option(names = { "-d", "--directory"}, description = "Output directory", required = true)
    private File outputDirectory;

    @Getter
    @Option(names = { "-s", "--site"}, description = "Available scrapers: ${COMPLETION-CANDIDATES}")
    private Site site = CENEO;

    @Getter
    @Option(names = { "-p", "--parserType"}, description = "Available output parsers: ${COMPLETION-CANDIDATES}")
    private ParserType parserType = XML;

    @Getter
    @Parameters(paramLabel = "URL", description = "Url which you want to access")
    private String url;

    private void validate() {
        if (isNull(url) || url.isEmpty())
            throw new WebScrapperException("Url must not be empty");
        if (!argumentValidator.isUrlValid(url))
            throw new WebScrapperException("Url is invalid");
        if (!argumentValidator.isAValidDirectory(outputDirectory))
            throw new WebScrapperException("Invalid output directory");
    }

    @Override
    public void run() {
        validate();
    }
}

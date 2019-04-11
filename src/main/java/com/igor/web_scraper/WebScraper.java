package com.igor.web_scraper;

import com.igor.web_scraper.cli.ArgumentValidator;
import com.igor.web_scraper.cli.CommandLineArguments;
import com.igor.web_scraper.exception.WebScrapperException;
import com.igor.web_scraper.exporter.ProductExporter;
import com.igor.web_scraper.output.FileSaver;
import com.igor.web_scraper.output.PathCreator;
import com.igor.web_scraper.parser.ParserFactory;
import com.igor.web_scraper.scraper.ScraperFactory;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.util.Objects;

@Slf4j
public class WebScraper {

    public static void main(String[] args) {
        try {
            CommandLineArguments cla = parseArguments(args);
            if (cla.isHelpRequested() || cla.isVersionInfoRequested())
                return;
            getProductExporter().export(cla);
        } catch (CommandLine.PicocliException | WebScrapperException e) {
            log.warn("Exception was thrown: {}", e);
            showUserFriendlyErrorMessage(e);
        }
    }

    private static CommandLineArguments parseArguments(String[] args) {
        CommandLineArguments cla = new CommandLineArguments(new ArgumentValidator());
        CommandLine commandLine = new CommandLine(cla);
        commandLine.setCaseInsensitiveEnumValuesAllowed(true);
        CommandLine.run(cla, args);
        return cla;
    }

    private static ProductExporter getProductExporter() {
        return new ProductExporter(new ScraperFactory(), new ParserFactory(), new FileSaver(), new PathCreator());
    }

    private static void showUserFriendlyErrorMessage(Exception e) {
        if (e instanceof WebScrapperException)
            System.out.println(e.getMessage());
        if (Objects.nonNull(e.getCause()) && e.getCause() instanceof WebScrapperException) {
            System.out.println(e.getCause().getMessage());
        } else {
            System.out.println("Unexpected error");
        }
    }

    public static void notifyUser(String message) {
        System.out.println(message);
    }
}

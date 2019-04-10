package unit.cli

import com.igor.web_scraper.cli.ArgumentValidator
import com.igor.web_scraper.cli.CommandLineArguments
import com.igor.web_scraper.parser.ParserType
import com.igor.web_scraper.scraper.Site
import picocli.CommandLine
import spock.lang.Specification

import static picocli.CommandLine.*

class CommandLineArgumentsUnitTest extends Specification {

    CommandLineArguments commandLineArguments
    ArgumentValidator argumentValidator

    def setup() {
        argumentValidator = Mock()
        commandLineArguments = new CommandLineArguments(argumentValidator)
    }

    def "should parse arguments"() {
        given:
        String[] args = ["-h", "-v", "-d", ".", "-s", "CENEO", "-p", "XML", "http://someurl.pl"]

        when:
        new CommandLine(commandLineArguments).parse(args)

        then:
        commandLineArguments.url == "http://someurl.pl"
        commandLineArguments.helpRequested
        commandLineArguments.versionInfoRequested
        commandLineArguments.outputDirectory.path == "."
        commandLineArguments.site == Site.CENEO
        commandLineArguments.parserType == ParserType.XML
    }

    def "CommandLineArguments class should use default arguments when were not provided"() {
        given:
        String[] args = ["-d", ".", "http://someurl.pl"]

        when:
        new CommandLine(commandLineArguments).parse(args)

        then:
        commandLineArguments.url == "http://someurl.pl"
        !commandLineArguments.helpRequested
        commandLineArguments.outputDirectory.path == "."
        commandLineArguments.site == Site.CENEO
        commandLineArguments.parserType == ParserType.XML
    }

    def "should throw ParameterException when wrong site type were provided"() {
        given:
        String[] args = ["-d", ".", "-s", "oenec", "http://someurl.pl"]

        when:
        new CommandLine(commandLineArguments).parse(args)

        then:
        thrown(ParameterException)
    }

    def "should throw ParameterException when wrong parser type were provided"() {
        given:
        String[] args = ["-d", ".", "-p", "XMR", "http://someurl.pl"]

        when:
        new CommandLine(commandLineArguments).parse(args)

        then:
        thrown(ParameterException)
    }

    def "should throw WebScraperException when url is not provided"() {
        given:
        String[] args = ["-d", "."]

        when:
        new CommandLine(commandLineArguments).parse(args)

        then:
        thrown(MissingParameterException)
    }

    def "should throw WebScraperException at validation when url empty"() {
        given:
        String[] args = ["-d", ".", ""]

        when:
        run(commandLineArguments, args)

        then:
        def exception = thrown(ExecutionException)
        exception.getCause().message == "Url must not be empty"
    }

    def "should throw WebScraperException at validation when url is invalid"() {
        given:
        String[] args = ["-d", ".", "bad:--url.ru"]
        argumentValidator.isUrlValid("bad:--url.ru") >> false

        when:
        run(commandLineArguments, args)

        then:
        def exception = thrown(ExecutionException)
        exception.getCause().message == "Url is invalid"
    }

    def "should throw WebScraperException at validation when output directory is not a valid directory"() {
        given:
        String[] args = ["-d", ".", "https://someUrl.pl"]
        argumentValidator.isUrlValid(_) >> true
        argumentValidator.isAValidDirectory(_) >> false

        when:
        run(commandLineArguments, args)

        then:
        def exception = thrown(ExecutionException)
        exception.cause.message == "Invalid output directory"
    }

    def "should validate at execution"() {
        given:
        String[] args = ["-d", ".", "https://someUrl.pl"]
        new CommandLine(commandLineArguments).parse(args)

        when:
        commandLineArguments.run()

        then:
        1 * argumentValidator.isUrlValid(_) >> true
        1 * argumentValidator.isAValidDirectory(_) >> true
    }
}

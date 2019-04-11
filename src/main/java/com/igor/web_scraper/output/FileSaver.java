package com.igor.web_scraper.output;

import lombok.NonNull;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.time.format.DateTimeFormatter.ofPattern;

public class FileSaver {

    private static final String FOLDER_DATE_FORMATTER = "yyyy-MM-dd_HH-mm-ss";

    public void saveToFile(@NonNull byte[] bytes, @NonNull File directory, String extension) throws IOException {
        String resultFilePath = directory.getPath()
                .concat(ZonedDateTime.now(ZoneId.of("Europe/Warsaw"))
                        .toLocalDateTime()
                        .format(ofPattern(FOLDER_DATE_FORMATTER)))
                .concat("/products")
                .concat(extension);

        FileUtils.writeByteArrayToFile(new File(resultFilePath), bytes);
    }
}

package com.igor.web_scraper.output;

import lombok.NonNull;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileSaver {

    public void saveToFile(@NonNull byte[] bytes, @NonNull File target) throws IOException {
        FileUtils.writeByteArrayToFile(target, bytes);
    }
}

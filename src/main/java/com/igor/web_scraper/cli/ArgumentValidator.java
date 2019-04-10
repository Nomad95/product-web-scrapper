package com.igor.web_scraper.cli;

import lombok.NonNull;

import java.io.File;
import java.util.regex.Pattern;

public class ArgumentValidator {

    private static final String URL_PATTERN = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    public boolean isUrlValid(@NonNull String url) {
        return Pattern.matches(URL_PATTERN, url);
    }

    //todo: check if has write
    public boolean isAValidDirectory(@NonNull File directory) {
        return directory.exists() && directory.isDirectory();
    }
}

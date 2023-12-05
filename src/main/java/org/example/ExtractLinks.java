package org.example;

import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractLinks {

    public static final Pattern URL_PATTERN = Pattern.compile("https?://[^\\s]+\\.html");


    public static CompletableFuture<Collection<String>> extractLinksAsync(String text) {
        return CompletableFuture.supplyAsync(() -> {
            Collection<String> links = new HashSet<>();
            Matcher matcher = URL_PATTERN.matcher(text);
            while (matcher.find()) {
                links.add(matcher.group());
            }
            return links;
        });

    }

}

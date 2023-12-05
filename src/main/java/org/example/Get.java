package org.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Get {

    public static CompletableFuture<List<String>> getHtmlResponsesAsync(Collection<String> links) throws IOException {
        List<CompletableFuture<String>> futures = new ArrayList<>();
        for (String link : links) {
            futures.add(getHtmlResponseAsync(link));
        }

        // Convert the `List<CompletableFuture<String>>` to an array of `CompletableFuture` objects
        CompletableFuture<Void> allFuturesCompletedFuture = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

        // Wait for all futures to complete
        allFuturesCompletedFuture.join();

        // Collect the results of the futures into a list
        List<String> htmlResponses = new ArrayList<>();
        for (CompletableFuture<String> future : futures) {
            htmlResponses.add(future.join());
        }

        return CompletableFuture.completedFuture(htmlResponses);
    }

    private static CompletableFuture<String> getHtmlResponseAsync(String link) throws IOException {
        return CompletableFuture.supplyAsync(() -> {
            Document document = null;
            try {
                document = Jsoup.connect(link).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return document.html();
        });
    }
}

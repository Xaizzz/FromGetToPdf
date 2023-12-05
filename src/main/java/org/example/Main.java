package org.example;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static void main(String[] args) {
        String text = "Geopolitics could drive oil prices over $100, Citi says\n" +
                "Oil prices could rise to $100 a barrel in the short term thanks to the latest developments out of Saudi Arabia and Russia, according to Citi.\n" +
                "https://www.cnbc.com/2023/11/08/disney-dis-board-in-focus-ahead-of-q4-earnings.html\n" +
                "2023-01-10 17:42:07.0\n" +
                "\n" +
                "Storm-ravaged Libya faces a long road to recovery as humanitarian groups ask for $71 million in aid\n" +
                "<p>This is the text inside a <p> tag that I want to extract.</p>\n" +
                "https://www.cnbc.com/2023/11/08/warner-bros-discovery-wbd-q3-earnings.html\n" +
                "2023-01-01 12:33:35.0\n" +
                "\n" +
                "Oil just hit its highest level of the year — and some analysts expect a return to $100 before 2024\n" +
                "<ul>This is the text inside a <ul> tag that I want to extract.</ul>\n" +
                "Oil prices climbed to their highest level of the year this week, extending a rally that has put a return to $100 a barrel sharply into focus.\n" +
                "https://www.cnbc.com/2023/11/02/paramount-global-para-earnings-q3-2023.html\n" +
                "2023-12-21 17:37:39.0";

        CompletableFuture<Collection<String>> linksFuture = ExtractLinks.extractLinksAsync(text);

        Get httpClient = new Get();
        CompletableFuture<List<String>> htmlResponsesFuture = linksFuture.thenCompose(links -> {
            try {
                return Get.getHtmlResponsesAsync(links);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        List<String> htmlResponses = htmlResponsesFuture.join();
        List<String> articleTitles = new ArrayList<>();

        for (String htmlResponse : htmlResponses) {
            org.jsoup.nodes.Document jsoupDocument = Jsoup.parse(htmlResponse);
            Elements h1Elements = jsoupDocument.select("h1.ArticleHeader-headline");

            for (Element h1Element : h1Elements) {
                String headerText = h1Element.text();
                articleTitles.add(headerText);
            }
        }

        String outputFolder = "C:/Users/Ilya/Downloads/test";

        // Извлекаем даты
        List<Date> dates = DateExtractor.extractDates(text);

        // Формируем название файла на основе дат
        String pdfFileName = generateFileName(outputFolder, dates);

        try {
            com.itextpdf.text.Document pdfDocument = new com.itextpdf.text.Document();
            PdfWriter.getInstance(pdfDocument, new FileOutputStream(pdfFileName));
            pdfDocument.open();

            // Оглавление
            StringBuilder tableOfContents = new StringBuilder("This file contains the following articles:\n");
            for (String title : articleTitles) {
                tableOfContents.append(title).append("\n");
            }

            // Добавляем двойной перевод строки перед статьями
            tableOfContents.append("\n\n");

            // Добавляем оглавление в PDF
            pdfDocument.add(new Paragraph(tableOfContents.toString()));

            // Добавляем статьи в PDF
            for (String htmlResponse : htmlResponses) {
                org.jsoup.nodes.Document jsoupDocument = Jsoup.parse(htmlResponse);
                Elements h1Elements = jsoupDocument.select("h1.ArticleHeader-headline");

                for (Element h1Element : h1Elements) {
                    String headerText = h1Element.text();

                    // Создаем Font для жирного текста
                    Font font = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);
                    Paragraph headerParagraph = new Paragraph();
                    headerParagraph.add(new Chunk(headerText, font));

                    // Добавляем заголовок статьи в PDF
                    pdfDocument.add(headerParagraph);

                    // Извлекаем текст только из <p> внутри <div class="group">
                    Elements groupDivs = jsoupDocument.select("div.group");

                    for (Element groupDiv : groupDivs) {
                        Elements pElements = groupDiv.select("p");
                        for (Element pElement : pElements) {
                            pdfDocument.add(new Paragraph(pElement.text()));
                        }
                    }

                    // Добавляем пустую строку между статьями
                    pdfDocument.add(Chunk.NEWLINE);
                }
            }

            // Закрываем PDF-документ после прохождения всех статей
            pdfDocument.close();

            System.out.println("PDF файл создан: " + pdfFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateFileName(String outputFolder, List<Date> dates) {
        SimpleDateFormat fileDateFormat = new SimpleDateFormat("yyyy_MM_dd");
        String startDateString = fileDateFormat.format(dates.get(0));
        String endDateString = fileDateFormat.format(dates.get(dates.size() - 1));
        return outputFolder + "/News_" + startDateString + "__" + endDateString + ".pdf";
    }
}


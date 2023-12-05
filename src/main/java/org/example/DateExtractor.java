package org.example;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DateExtractor {

    public static List<Date> extractDates(String text) {
        List<Date> dates = new ArrayList<>();
        String[] words = text.split("\\s+");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i < words.length - 2; i++) {
            if (words[i + 2].matches("\\d{4}-\\d{2}-\\d{2}")) {
                String dateString = words[i + 2];
                try {
                    Date date = sdf.parse(dateString);
                    dates.add(date);
                } catch (ParseException e) {
                    e.printStackTrace(); // Обработка ошибок парсинга даты
                }
            }
        }

        return dates;
    }
}

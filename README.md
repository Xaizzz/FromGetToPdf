**Проектная Документация**

---

### 1. Класс `DateExtractor`

**Описание:**
Класс `DateExtractor` предоставляет функционал для извлечения дат из текста.

**Методы:**
1. **`extractDates(String text)`**
   - **Описание:** Извлекает даты из текста и возвращает список объектов `Date`.
   - **Параметры:**
     - `text` - исходный текст, из которого извлекаются даты.
   - **Возвращаемое значение:** Список объектов `Date`, представляющих извлеченные даты.

---

### 2. Класс `ExtractLinks`

**Описание:**
Класс `ExtractLinks` предоставляет функционал для асинхронного извлечения ссылок из текста.

**Методы:**
1. **`extractLinksAsync(String text)`**
   - **Описание:** Асинхронно извлекает ссылки из текста и возвращает коллекцию строк с найденными ссылками.
   - **Параметры:**
     - `text` - исходный текст, из которого извлекаются ссылки.
   - **Возвращаемое значение:** `CompletableFuture`, завершаемый коллекцией строк - найденными ссылками.

---

### 3. Класс `Get`

**Описание:**
Класс `Get` предоставляет функционал для асинхронного получения HTML-ответов по переданным ссылкам.

**Методы:**
1. **`getHtmlResponsesAsync(Collection<String> links)`**
   - **Описание:** Асинхронно получает HTML-ответы по коллекции ссылок.
   - **Параметры:**
     - `links` - коллекция строк с ссылками.
   - **Возвращаемое значение:** `CompletableFuture`, завершаемый списком строк - HTML-ответами.

2. **`getHtmlResponseAsync(String link)`**
   - **Описание:** Асинхронно получает HTML-ответ по конкретной ссылке.
   - **Параметры:**
     - `link` - строка с URL-адресом.
   - **Возвращаемое значение:** `CompletableFuture`, завершаемый строкой - HTML-ответом.

---

### 4. Класс `Main`

**Описание:**
Класс `Main` содержит основную логику программы для создания PDF-файла на основе текста, извлеченного из HTML-страниц.

**Методы:**
1. **`main(String[] args)`**
   - **Описание:** Главный метод программы, который выполняет основную логику.
   - **Параметры:**
     - `args` - аргументы командной строки (не используются).
   - **Возвращаемое значение:** Метод ничего не возвращает, так как это `void` метод.

2. **`generateFileName(String outputFolder, List<Date> dates)`**
   - **Описание:** Метод для формирования названия файла на основе дат.
   - **Параметры:**
     - `outputFolder` - путь к папке, в которой будет создан файл.
     - `dates` - список дат, на основе которых формируется название файла.
   - **Возвращаемое значение:** Название файла в виде строки.

---

**Примечания:**
- Программа использует библиотеки `Jsoup` для парсинга HTML и `iText` для создания PDF-файла.
- Асинхронные операции выполняются с использованием `CompletableFuture`.
- Проект охватывает функционал извлечения дат, ссылок и HTML-ответов, а также создание PDF-файла с оглавлением и статьями.

**Класс `DateExtractor`**

**Описание:**
Класс `DateExtractor` предназначен для извлечения дат из текстовой строки.

**Методы:**
1. **`extractDates(String text)`**
   - **Описание:** Метод для извлечения дат из переданного текста.
   - **Параметры:**
     - `text` - текст, из которого требуется извлечение дат.
   - **Возвращаемое значение:** Список объектов `Date`, представляющих извлеченные даты.
   - **Исключения:** `ParseException` - в случае ошибки при парсинге даты.

**Пример использования:**
```java
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

List<Date> dates = DateExtractor.extractDates(text);
```

**Примечание:**
- Класс использует `SimpleDateFormat` для парсинга строк в формате "yyyy-MM-dd".
- Извлеченные даты добавляются в список, который возвращается методом.

**Класс `ExtractLinks`**

**Описание:**
Класс `ExtractLinks` предназначен для асинхронного извлечения URL-ссылок из текстовой строки.

**Поля:**
1. **`URL_PATTERN`**
   - **Описание:** Паттерн для поиска URL-ссылок в тексте.
   - **Тип:** `Pattern`
   - **Значение:** `https?://[^\\s]+\\.html`

**Методы:**
1. **`extractLinksAsync(String text)`**
   - **Описание:** Асинхронный метод для извлечения URL-ссылок из переданного текста.
   - **Параметры:**
     - `text` - текст, из которого требуется извлечение URL-ссылок.
   - **Возвращаемое значение:** `CompletableFuture` содержащий коллекцию строк - извлеченные URL-ссылки.

**Пример использования:**
```java
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
```

**Примечание:**
- Используется `CompletableFuture` для асинхронного выполнения операции извлечения URL-ссылок.
- Паттерн `URL_PATTERN` используется для поиска ссылок в тексте с учетом протокола (`http` или `https`) и формата (`html`).

**Класс `Get`**

**Описание:**
Класс `Get` предоставляет функциональность для асинхронного получения HTML-ответов по URL-ссылкам.

**Методы:**
1. **`getHtmlResponsesAsync(Collection<String> links)`**
   - **Описание:** Асинхронный метод для получения HTML-ответов по коллекции URL-ссылок.
   - **Параметры:**
     - `links` - коллекция URL-ссылок.
   - **Возвращаемое значение:** `CompletableFuture` содержащий список HTML-ответов в виде строк.

2. **`getHtmlResponseAsync(String link)`**
   - **Описание:** Асинхронный метод для получения HTML-ответа по одной URL-ссылке.
   - **Параметры:**
     - `link` - URL-ссылка.
   - **Возвращаемое значение:** `CompletableFuture` содержащий HTML-ответ в виде строки.

**Пример использования:**
```java
Collection<String> links = new HashSet<>();
links.add("https://www.example.com/page1.html");
links.add("https://www.example.com/page2.html");

CompletableFuture<List<String>> htmlResponsesFuture = Get.getHtmlResponsesAsync(links);
List<String> htmlResponses = htmlResponsesFuture.join();
```

**Примечание:**
- Используется библиотека Jsoup для выполнения HTTP-запросов и получения HTML-ответов.
- Асинхронные методы возвращают объекты типа `CompletableFuture`, обеспечивая асинхронное выполнение операций.

**Класс `Main`**

**Описание:**
Класс `Main` содержит основную логику программы для создания PDF-файла на основе текста, извлеченного из HTML-страниц.

**Методы:**
1. **`main(String[] args)`**
   - **Описание:** Главный метод программы, который выполняет основную логику.
   - **Параметры:**
     - `args` - аргументы командной строки (не используются).
   - **Возвращаемое значение:** Метод ничего не возвращает, так как это `void` метод.

2. **`generateFileName(String outputFolder, List<Date> dates)`**
   - **Описание:** Метод для формирования названия файла на основе дат.
   - **Параметры:**
     - `outputFolder` - путь к папке, в которой будет создан файл.
     - `dates` - список дат, на основе которых формируется название файла.
   - **Возвращаемое значение:** Название файла в виде строки.

**Пример использования:**
```java
public static void main(String[] args) {
    // ... (код предыдущих частей программы)

    // Формирование названия файла на основе дат
    String pdfFileName = generateFileName(outputFolder, dates);

    // ... (код создания PDF и добавления статей)

    System.out.println("PDF файл создан: " + pdfFileName);
}
```

**Примечание:**
- В программе используются библиотеки `Jsoup` для парсинга HTML и `iText` для создания PDF-файла.
- Программа асинхронно извлекает ссылки из текста, получает HTML-ответы для каждой ссылки и извлекает заголовки статей.
- Формируется оглавление файла, которое включает названия статей с переносами строки, перед тем как добавить статьи в PDF.

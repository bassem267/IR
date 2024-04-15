package Index;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class WebCrawler {
    private Set<String> links = new HashSet<>();
    private int pageCount = 0;
    private static final int MAX_PAGES = 10; // Maximum number of pages to crawl

    public void getPageLinks(String URL) {
        //4. Check if you have already crawled the URLs
        //(we are intentionally not checking for duplicate content in this example)
        assert links != null;
        if (!links.contains(URL) && pageCount < MAX_PAGES) {
            try {
                //4. (i) If not add it to the index
                if (links.add(URL)) {
                    System.out.println(URL);
                    pageCount++; // Increment the page counter
                }

                //2. Fetch the HTML code
                Document document = Jsoup.connect(URL).get(); //jsoup jar to extract web data

                // Use Jsoup default text extraction to get the main content
                String mainContent = document.text();

                // Save page content to a text file
                savePageContent(URL, mainContent);

                //3. Parse the HTML to extract links to other URLs
                Elements linksOnPage = document.select("a[href]");

                //5. For each extracted URL... go back to Step 4.
                for (Element page : linksOnPage) {
                    getPageLinks(page.attr("abs:href"));
                }
            } catch (IOException e) {
                System.err.println("For '" + URL + "': " + e.getMessage());
            }
        }
    }

    private void savePageContent(String URL, String content) throws IOException {
        String fileName = URL.replaceAll("[^a-zA-Z0-9-_\\.]", "_") + ".txt";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
        }
    }
}




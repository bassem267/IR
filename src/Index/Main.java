package Index;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import java.lang.*;

public class Main {

    public static void main(String[] args) {

        PositionalIndex PositionalIndex = new PositionalIndex(new String[]{
                "Doc1.txt",
                "Doc2.txt",
                "Doc3.txt",
                "Doc4.txt",
                "Doc5.txt",
                "Doc6.txt",
                "Doc7.txt",
                "Doc8.txt",
                "Doc9.txt",
                "Doc10.txt",
        });
        Scanner scanner = new Scanner(System.in);

        label:
        while (true) {
            System.out.print("""
                    Enter 1 For Cosine Similarity \s
                    Enter 2 For Positional Index \s
                    Enter 3 For TF-IDF \s
                    Enter 4 For Inverted Index \s
                    Enter 5 For Web Crawler \s
                    Enter 0 To Exit:\s""");
            String choice = scanner.nextLine();
            String query;

            switch (choice) {
                case "0":
                    break label;
                case "1":
                {
                    System.out.print("Enter Query: ");
                    query = scanner.nextLine();

                    // Create an ArrayList from the array
                    ArrayList<String> words = new ArrayList<>(Arrays.asList(query.split("\\W+")));

                    PositionalIndex.cSimilarity(words);
                    System.out.println();

                    break;
                }
                // positional index
                case "2":
                {
                    System.out.print("Enter Query: ");
                    query = scanner.nextLine();

                    PositionalIndex.findQuery(query);
                    System.out.println();
                    break;
                }
                // To Get TD-IDF For a Query
                case "3":
                {
                    System.out.print("Enter Query: ");
                    query = scanner.nextLine();

                    // Create an ArrayList from the Query
                    ArrayList<String> words = new ArrayList<>(Arrays.asList(query.split("\\W+")));

                    PositionalIndex.SearchForPhrase(words);
                    System.out.println();

                    break;
                }
                // Normal Inverted Index Search
                case "4":
                {
                    System.out.print("Enter Query: ");
                    query = scanner.nextLine();

                    PositionalIndex.SearchInvertedIndex(query);

                    break;
                }
                // Web Crawler
                case "5":
                {
                    System.out.print("Enter Query: ");
                    //String url = scanner.nextLine();

                    WebCrawler crawler = new WebCrawler();
                    crawler.getPageLinks("https://en.wikipedia.org/wiki/Inverted_index");

                    break;
                }
            }
        }
    }
}

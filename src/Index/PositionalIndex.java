package Index;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PositionalIndex {

    int ND;
    HashMap<String, DictEntry> index = new HashMap<>();
    ArrayList<Integer> DSize = new ArrayList<>();
    ArrayList<List<Double>> WeightMatrix = new ArrayList<>();

    String[] stopWords = {"a", "as", "able", "about",
            "above", "according", "accordingly", "across", "actually",
            "after", "afterwards", "again", "against", "ain't", "all",
            "allow", "allows", "almost", "alone", "along", "already",
            "also", "although", "always", "am", "among", "amongst", "an",
            "and", "another", "any", "anybody", "anyhow", "anyone", "anything",
            "anyway", "anyways", "anywhere", "apart", "appear", "appreciate",
            "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking",
            "associated", "at", "available", "away", "awfully", "be", "became", "because",
            "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being",
            "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both",
            "brief", "but", "by", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes",
            "certain", "certainly", "changes", "clearly", "co", "com", "come",
            "comes", "concerning", "consequently", "consider", "considering", "contain",
            "containing", "contains", "corresponding", "could", "couldn't", "course", "currently",
            "definitely", "described", "despite", "did", "didn't", "different", "do", "does",
            "doesn't", "doing", "don't", "done", "down", "downwards", "during", "each", "edu",
            "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially",
            "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere",
            "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "five", "followed",
            "following", "follows", "for", "former", "formerly", "forth", "four", "from", "further",
            "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone"
            , "got", "gotten", "greetings", "had", "hadn't", "happens", "hardly", "has", "hasn't", "have",
            "haven't", "having", "he", "hes", "hello", "help", "hence", "her", "here", "here's", "hereafter",
            "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully",
            "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch",
            "inc", "indeed", "indicate", "indicated", "indicates", "inner", "insofar", "instead", "into", "inward", "is",
            "isn't", "it", "itd", "it'll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known",
            "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely",
            "little", "look", "looking", "looks", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely",
            "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly",
            "necessary", "need", "needs", "neither", "never", "nevertheless", "new", "next", "nine", "no", "nobody", "non", "none",
            "none", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok",
            "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours",
            "ourselves", "out", "outside", "over", "overall", "own", "particular", "particularly", "per", "perhaps", "placed", "please",
            "plus", "possible", "presumably", "probably", "provides", "que", "quite", "qv", "rather", "rd", "re", "really", "reasonably",
            "regarding", "regardless", "regards", "relatively", "respectively", "right", "said", "same", "saw", "say", "saying", "says",
            "second", "secondly", "see", "seeing", "seem", "seemed", "seeming", "seems", "seen", "self", "selves", "sensible", "sent",
            "serious", "seriously", "seven", "several", "shall", "she", "should", "shouldn't", "since", "six", "so", "some", "somebody",
            "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "specify",
            "specifying", "still", "sub", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thank", "thanks",
            "than", "that", "that's", "that's", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter",
            "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "'they'd'", "they'll", "they're", "they've", "think", "third",
            "this", "thorough", "thoroughly", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "took",
            "toward", "towards", "tried", "tries", "truly", "try", "trying", "twice", "two", "un", "under", "unfortunately", "unless", "unlikely",
            "until", "unto", "up", "upon", "us", "use", "used", "useful", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs",
            "want", "wants", "was", "wasn't", "way", "we", "wed", "well", "were", "we've", "welcome", "well", "went", "were", "weren't", "what",
            "whats", "whatever", "when", "whence", "whenever", "where", "wheres", "thereafter", "whereas", "whereby", "wherein", "whereupon", "wherever",
            "whether", "which", "while", "whither", "who", "who's", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with",
            "within", "without", "wont", "wonder", "would", "would", "wouldn't", "yes", "yet", "you", "you'd", "you'll", "you're", "you've", "your",
            "yours", "yourself", "yourselves", "zero"};

    public PositionalIndex(String[] files) {
        buildIndex(files);
        updateIDF();
        // to Print The Index
        printKeys();
        generateWeightMatrix();
    }

    public void updateIDF() {
        for (String keys : index.keySet()) {
            index.get(keys).IDF = Math.log10((double) ND / (double) index.get(keys).doc_freq);
        }
    }

    public void buildIndex(String[] files) {
        int i = 0;
        int wordCount;

        for (String fileName : files) {
            i++;
            //number of documents
            this.ND = i;
            try (BufferedReader file = new BufferedReader(new FileReader(fileName))) {
                //sources.put(i, fileName); for the other function
                String ln;
                wordCount = 0;

                while ((ln = file.readLine()) != null) {
                    // Convert the array to a list
                    ArrayList<String> words = new ArrayList<>(Arrays.asList(ln.split("\\W+")));

                    // Iterate over the list in reverse order
                    for (int z = words.size() - 1; z >= 0; z--) {
                        // get the item as string
                        for (String stopWord : stopWords) {
                            if (stopWord.contains(words.get(z))) {
                                words.remove(z);
                                break;  // break out of the inner loop to avoid ConcurrentModificationException
                            }
                        }
                    }

                    wordCount += words.size();

                    //loop over the word within the txt file
                    Integer wordNum=0;
                    for (String word : words) {
                        wordNum++;
                        word = word.toLowerCase();

                        if (!index.containsKey(word)) {//in case of it is a new term
                            Posting pp = new Posting(i, 1);//create posting with the current file ID
                            pp.positions.add(wordNum);
                            DictEntry dc = new DictEntry(pp);
                            index.put(word, dc);
                        } else {//if it's an existing term
                            DictEntry de = index.get(word);
                            de.term_freq++;
                            Posting pp = de.pList;//head

                            while (pp != null) {//loop over the linked list

                                if (i == pp.docId) {//if the term exists in the same document
                                    pp.dtf++;
                                    pp.positions.add(wordNum);
                                    break;
                                }
                                if (pp.next == null) {//if not insert it at the end of the linked-list
                                    pp.next = new Posting(i, 1);
                                    pp.next.positions.add(wordNum);
                                    de.doc_freq++;
                                    break;
                                }
                                pp = pp.next;

                            }
                        }
                    }
                }
                DSize.add(wordCount); //store the size of the current file in DSize

            } catch (IOException e) {
                System.out.println("File " + fileName + " not found. Skip it");
            }
        }
    }

    public void printKeys() {
        TreeMap<String, DictEntry> tm = new TreeMap<>(index);

        for (String keys : tm.keySet()) {
            System.out.print("'" + keys + "'  ( " + index.get(keys).doc_freq + " ) ---> ( ");
            DictEntry temp = index.get(keys);
            temp.print();
        }
        System.out.println("Documents Sizes :");
        System.out.println(DSize);
    }

    public void generateWeightMatrix() {
        TreeMap<String, DictEntry> TermIndex = new TreeMap<>(index);

        ArrayList<List<Double>> matrix2D = new ArrayList<>();

        // get all documents tf-idf for each term
        for (DictEntry Term : TermIndex.values()) {
            ArrayList<Double> row = new ArrayList<>(); // create a new row for each term
            for (int i = 1; i <= ND; i++) {
                Posting temp = Term.getPostingID(String.valueOf(i));
                if (temp != null) {
                    //calculate tf-idf
                    double TF =(double)temp.dtf/ (double) DSize.get(temp.docId-1);
                    Double result = ((1 + Math.log10(TF)) * Term.IDF);
                    row.add(result);
                } else
                    row.add(0.0);
            }
            matrix2D.add(row);
        }

        WeightMatrix = matrix2D;
    }

    public void cSimilarity(ArrayList<String> words) {
        TreeMap<String, DictEntry> TermIndex = new TreeMap<>(index);
        ArrayList<Double> Score= new ArrayList<>();

        //1 removing any stop words
        for (int z = words.size() - 1; z >= 0; z--) {
            // get the item as string
            for (String stopWord : stopWords) {
                if (stopWord.contains(words.get(z))) {
                    words.remove(z);
                    break;  // break out of the inner loop to avoid ConcurrentModificationException
                }
            }
        }

        // get all documents tf-idf for each term
        ArrayList<Double> row = new ArrayList<>(); // create a new row for each term
        for (String Term : TermIndex.keySet()) {

            Double nAcc = countN(words, Term); //number of occurrence
            if (nAcc != 0.0) {
                //calculate tf-idf
                Double result = ((1 + Math.log10(nAcc/(double)words.size())) * TermIndex.get(Term).IDF);
                row.add(result);
                //System.out.println(result);
            }
            else
                row.add(0.0);
        }
        ArrayList<Double> tempVec = new ArrayList<>();
        // create a new row for each term

        // get each document frequency
        for(int i=0; i<ND;i++) {
            for (List<Double> row1 : WeightMatrix) {
                tempVec.add(row1.get(i));
            }
            double dotProduct = dotProduct(row, tempVec);
            double norm1 = norm(row);
            double norm2 = norm(tempVec);
            double similarity = dotProduct / (norm1 * norm2);
            Score.add(similarity);
            tempVec.clear();
        }


        // print the results
        List<Integer> documentNumbers = new ArrayList<>();
        for (int z = 1; z <= ND; z++) {
            documentNumbers.add(z);
        }

        documentNumbers.sort((a, b) -> Double.compare(Score.get(b - 1), Score.get(a - 1)));

        for (int i = 0; i < ND; i++) {
            int documentNumber = documentNumbers.get(i);
            double score = Score.get(documentNumber - 1);
            System.out.println("Document " + documentNumber + " Score is " + score);
        }
    }

    public Double countN(ArrayList<String> words, String Term) {
        Double count = 0.0;
        for (String word : words) {
            if (word.equals(Term))
                count++;
        }
        return count;
    }

    public static double dotProduct(List<Double> v1, List<Double> v2) {
        double dotProduct = 0;
        for (int i = 0; i < v1.size(); i++) {
            dotProduct += v1.get(i) * v2.get(i);
        }
        return dotProduct;
    }

    // Calculate the norm of a vector
    public static double norm(List<Double> v) {
        double norm = 0;
        for (Double aDouble : v) {
            norm += aDouble * aDouble;
        }
        norm = Math.sqrt(norm);
        return norm;
    }

    public void findQuery(String phrase) {
        ArrayList<ArrayList<Integer>> matchList = new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            ArrayList<Integer> test=new ArrayList<>();
            matchList.add(test);
        }

        String[] terms = phrase.split(" ");

        int len=terms.length;
        for (String term : terms) {
            if (index.containsKey(term)) {
                Posting positionsList = index.get(term).pList;
                while (positionsList != null) {
                    if (matchList.get(positionsList.docId - 1).size() != 0) {
                        if (matchList.get(positionsList.docId - 1).get(matchList.get(positionsList.docId - 1).size() - 1) == positionsList.positions.get(0)-1) {
                            matchList.get(positionsList.docId - 1).add(positionsList.positions.get(0));
                        }
                    } else {

                        matchList.get(positionsList.docId - 1).add(positionsList.positions.get(0));
                    }
                    positionsList=positionsList.next;
                }
            }
        }
        int i=1;
        ArrayList<Integer> query = new ArrayList<>();
        for(ArrayList watchlist:matchList)
        {
            if(watchlist.size()==len)
                query.add(i);
            i++;
        }

        System.out.println("The Query: " + phrase + " , Found in Documents " + query);
    }

    public ArrayList<String> SearchForWord(String w) {
        DictEntry entry = index.get(w);
        ArrayList<String> doc = new ArrayList<>();

        // check if word exist in files or not
        if (entry != null) {
            Posting pp = entry.pList;
            while (pp != null) {
                doc.add(String.valueOf(pp.docId));
                pp = pp.next;
            }
        }

        return doc;
    }

    public void SearchForPhrase(ArrayList<String> words) {
        // remove stop words from
        for (int z = words.size() - 1; z >= 0; z--) {
            // get the item as string
            for (String stopWord : stopWords) {
                if (stopWord.contains(words.get(z))) {
                    words.remove(z);
                    break;  // break out of the inner loop to avoid ConcurrentModificationException
                }
            }
        }
        // HashSet  that stores the matched doc of each term
        //merge
        HashSet<String> docs = new HashSet<>(merge(words));

        //now calculate each
        double weight, docWeight;

        for (String docT : docs) {
            docWeight = 0.0;
            System.out.println("Document ID : " + docT);
            System.out.println("Document size " + DSize.get(Integer.parseInt(docT) - 1));

            for (String word : words) {

                //get dic entry
                DictEntry dictEntry = index.get(word);
                double TF =(double)dictEntry.getPostingID(docT).dtf/ (double)DSize.get(dictEntry.getPostingID(docT).docId-1);
                double tf = (1 + Math.log10(TF));
                int termFrequency = dictEntry.getPostingID(docT).dtf;

                //gets every posting for each document and calculate the tf-idf
                System.out.println("Term Frequency for " + word + " is:" + termFrequency );
                java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#0.00");

                String formattedNumber = decimalFormat.format(dictEntry.IDF);

                System.out.println("IDF is: " + formattedNumber);

                formattedNumber = decimalFormat.format(tf);
                System.out.println("Tf is : " + formattedNumber);

                //calculate tf
                //double TF = (1 + Math.log10(dictEntry.getPostingID(docT).getTf()));
                //tf-idf
                weight = tf * dictEntry.IDF;
                docWeight += weight;
            }
            java.text.DecimalFormat decimalFormat = new java.text.DecimalFormat("#0.00");
            String formattedNumber = decimalFormat.format(docWeight);
            System.out.println("\ndoc " + docT + " weight is " + formattedNumber);
        }
    }

    public ArrayList<String> merge(ArrayList<String> words) {
        ArrayList<ArrayList<String>> arrayOfLists = new ArrayList<>();

        for (String word : words) {
            arrayOfLists.add(SearchForWord(word));
        }

        ArrayList<String> intersection = new ArrayList<>(arrayOfLists.get(0));
        for (int i = 1; i < arrayOfLists.size(); i++) {
            ArrayList<String> nextList = arrayOfLists.get(i);
            intersection.retainAll(nextList);
        }

        return intersection;
    }

    public void SearchInvertedIndex(String w) {
        DictEntry entry = index.get(w);
        // check if word exist in files or not
        if (entry != null) {
            System.out.println(w + " is found in Documents :)");
            System.out.println("Document Frequency is :   " + entry.doc_freq);
            System.out.println("Term Frequency is :   " + entry.term_freq);

            Posting pp = entry.pList;
            while (pp != null) {
                System.out.println("its Frequency in document # " + pp.docId + ": " + pp.dtf + " times");
                pp = pp.next;
            }
            System.out.println();
        }
        else {
            System.out.println("The Word (" + w + ") is not found in Documents :(");
        }
    }
}

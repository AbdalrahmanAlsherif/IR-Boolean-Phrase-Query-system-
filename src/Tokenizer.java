import java.util.ArrayList;
import java.util.HashMap;

public class Tokenizer {
    public static String[] tokenize(String document){
        Stemmer stemmer=new Stemmer();
        String line = document.replaceAll("\n"," ");
        String[] terms = line.split(" ");
        for (int i = 0; i < terms.length; i++) {
            String term = terms[i];
            term = term.trim();
            term = term.replaceAll("[.,]", "");
            term = term.toLowerCase();
            terms[i]=stemmer.stem(term);
        }
        return terms;
    }
}

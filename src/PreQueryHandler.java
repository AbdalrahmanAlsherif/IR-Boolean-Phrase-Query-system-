import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class PreQueryHandler {
    HashMap<String, IndexData> positionalIndex;
    ArrayList<String> tokens=new ArrayList<>();
    ArrayList<String> documents;

    public PreQueryHandler(String path){
        documents=new ArrayList<>();
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file :files){
                documents.add(file.getName());
            }
        }else{
            System.out.println("No Files");
        }
        positionalIndex =new HashMap<>();
        try {
            readDoc(path);
        } catch (IOException e) {
            throw new RuntimeException("Error with readDoc function, visit PreprocessHandler");
        }
    }



    private void readDoc (String path) throws IOException {
        File folder = new File(path);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                String document="";
                if (file.isFile()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(path + "/" + file.getName()))) {
                        document = document+ br.readLine();
                    }
                    String[] tokenized = Tokenizer.tokenize(document);
                    for (int i = 0; i < tokenized.length; i++) {
                        IndexData item;
                        if (!positionalIndex.containsKey(tokenized[i])) {
                            item = new IndexData(tokenized[i], file.getName(), i);
                            tokens.add(tokenized[i]);

                        } else {
                            item = positionalIndex.get(tokenized[i]);
                            item.addTokenEntry(file.getName(), i);
                        }
                        positionalIndex.put(tokenized[i], item);
                    }
                }
            }
        }
    }


    public int getPositions(String token,String docName) {
        if (positionalIndex.get(token).positions.get(docName)!=null){
            return positionalIndex.get(token).positions.get(docName).size();
        }
        return 0;
    }

}

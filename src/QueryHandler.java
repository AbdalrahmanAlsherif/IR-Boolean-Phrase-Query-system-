import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class QueryHandler {

    HashMap<String, IndexData> positionalIndex;
    String[] docName;

    ArrayList<String>result;

    public QueryHandler(String query, HashMap<String, IndexData> positionalIndex,String[] docName){
        this.positionalIndex=positionalIndex;
        this.docName=docName;
        if(query.contains("AND")||query.contains("OR")||query.contains("NOT")){
            boolQuery(infixToPostfix(query));
        }
        else{
            result = phraseQuery(query);
        }
    }


    /*
    public void phraseQuery(String query){
        String[] hold=Tokenizer.tokenize(query);
        ArrayList<IndexData> tokenData=new ArrayList<>();
        int flag=0;
        //stems tokens
        for (int i=0;i<hold.length;i++) {
            Stemmer stemmer=new Stemmer(hold[i]);
            String  stemmedToken= new String(stemmer.stem());
            processedQuery.set(i,stemmedToken);
        }
        //handles if Query terms are not in the posting list
        for (String token:processedQuery){
            if (positionalIndex.containsKey(token)) flag++;
        }
        if (flag!=processedQuery.size()) System.out.println("not in documents");
        //======handles if Query terms are in the posting list=========
        //retrieves data about all tokens in the processedQuery
        for (String s : processedQuery) tokenData.add(positionalIndex.get(s));
        commonDocs();
        for (int i =0;i<commonDocs.size();i++){
            boolean resultFlag = true;
            Integer[] arrangements=new Integer[processedQuery.size()];
            for (int j=0;j<processedQuery.size();j++){
                if (arrangements[0]!=null){
                    if(!arrangements[i-1].equals(positionalIndex.get(processedQuery.get(i)).positions.get(commonDocs.get(i)).get(j))){
                        arrangements=null;
                    }
                }
            }
            if(arrangements != null) {
                resultFlag = true;
                for(int k = 0; k < arrangements.length - 1; k++) {
                    if(arrangements[k+1] - arrangements[k] != 1) {
                        resultFlag = false;
                        break;
                    }
                }
            }
            if(resultFlag) {
                System.out.println("Phrase found in document: " + commonDocs.get(i));
            }
        }
    }*/

    public ArrayList<String> phraseQuery(String query) {
        ArrayList<String> processedQuery = new ArrayList<>();
        String[] hold = Tokenizer.tokenize(query);
        ArrayList<String> docs=new ArrayList<>();
        ArrayList<String> commonDocs=new ArrayList<>();


        // Stems tokens
        for (String s : hold) {


        }

        // Handles if Query terms are not in the posting list
        for (String token : processedQuery) {
            if (!positionalIndex.containsKey(token)) {
                System.out.println("Token " + token + " not in documents");
                return docs;
            }
        }

        // Handles if Query terms are in the posting list
        commonDocs(processedQuery,commonDocs);

        for (String doc : commonDocs) {
            boolean resultFlag = false; // Initialize to false

            ArrayList<Integer> firstTokenPositions = positionalIndex.get(processedQuery.get(0)).positions.get(doc);

            if (firstTokenPositions != null) {
                for (Integer startPos : firstTokenPositions) {
                    boolean currentTokenFound = true;

                    for (int i = 1; i < processedQuery.size(); i++) {
                        ArrayList<Integer> currentTokenPositions = positionalIndex.get(processedQuery.get(i)).positions.get(doc);

                        if (currentTokenPositions == null || !currentTokenPositions.contains(startPos + i)) {
                            currentTokenFound = false;
                            break;
                        }
                    }

                    if (currentTokenFound) {
                        resultFlag = true;
                        break; // Phrase found, no need to check other positions
                    }
                }
            }

            if (resultFlag) {
                docs.add(doc);
            } else {
                System.out.println("Phrase not found in document: " + doc);
            }
        }
        return docs;
    }

    public void boolQuery(ArrayList<String> postFixedQ){
        Stack<ArrayList<String>> phrase=new Stack<>();
        for (String item:postFixedQ){
            if(!(item.equals("AND") || item.equals("OR") || item.equals("NOT"))){
                phrase.push(phraseQuery(item));
            }else if(item.equals("AND") || item.equals("OR")){
                ArrayList<String>q1= (ArrayList<String>) phrase.pop();
                ArrayList<String>q2= (ArrayList<String>) phrase.pop();
                switch(item){
                    case "AND":
                        phrase.push(intersect(q1,q2));
                        break;
                    case "OR":
                        q1.addAll(q2);
                        phrase.push(q1);
                        break;
                }
            }else{
                phrase.pop();
            }
        }
    }

    public void commonDocs(ArrayList<String> processedQuery, ArrayList<String> commonDocs){
        for (String string : docName) {
            boolean flag = true;
            for (String s : processedQuery) {
                if (!positionalIndex.get(s).positions.containsKey(string)) {
                    flag = false;
                }
            }
            if (flag) {
                commonDocs.add(string);
            }
        }
    }

    public ArrayList<String> infixToPostfix(String statement){
        String []words=statement.split(" ");
        ArrayList<String> phrases=new ArrayList<>();
        ArrayList<String>postfix=new ArrayList<>();
        Stack<String> operators=new Stack<>();
        int startingPhraseIndex=0;
        for (int i=0;i<words.length;i++) {
            if ((words[i].equals("AND") || words[i].equals("OR") || words[i].equals("NOT"))) {
                StringBuilder temp= new StringBuilder();
                for (int j=startingPhraseIndex;j<i;j++){
                    temp.append(" ").append(words[j]);
                }
                phrases.add(temp.toString());
            }
            phrases.add(words[i]);
        }
        for (String s:phrases){
            if (s.equals("AND") || s.equals("OR") || s.equals("NOT")){
                while (!operators.empty()&&checkPrecedence(s,operators.peek())){
                    postfix.add(operators.pop());
                }
                operators.push(s);
            } else{
                postfix.add(s);
            }
        }
        while(!operators.empty()){
            postfix.add(operators.pop());
        }

        postfix.addAll(operators);
        return postfix;
    }
    public boolean checkPrecedence(String op1,String op2){
        return precedence(op1)<precedence(op2);
    }
    public int precedence(String op){
        if (op.equals("NOT")){
            return 3;
        }else if (op.equals("AND")){
            return 2;
        }else if(op.equals("OR")){
            return 1;
        }else{
            return 4;
        }
    }

    public ArrayList<String>intersect(ArrayList<String>q1,ArrayList<String>q2){
        ArrayList<String> list = new ArrayList<String>();
        for (String t : q1) {
            if(q2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }
}

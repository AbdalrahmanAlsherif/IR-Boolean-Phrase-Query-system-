import  java.util.ArrayList;
import java.util.HashMap;

public class VectorSpace {
    HashMap<String, IndexData> index;
    int totalDocuments;
    private String term;

    public VectorSpace(HashMap<String, IndexData> index) {
        this.index = index;
        totalDocuments = 10;
    }
   public double computeTF(String term, IndexData item ){
        double tf = 0.0; //initialized to a default value
        ArrayList<Integer> positions =item.positions.get("query");
        if (positions!= null){
             tf =(double) positions.size() / (double) item.tFrequency;
        }
       return 1+ Math.log(tf);
    }

    //old
//    double computeDF(String term ){
//        int documentFrequency = 0;
//        for (HashMap.Entry<String , IndexData> entry : index.entrySet()){
//            IndexData item =entry.getValue();
//            if (item.positions.containsKey(term)){
//                documentFrequency++;
//            }
//        }
//        return documentFrequency;
//    }

    //New and correct output
    double computeDF2(String term) {
        int documentFrequency = 0;

        if (index.containsKey(term)) {
            IndexData item = index.get(term);

            // Check if the term is present in any document in the 'positions' map
            for (HashMap.Entry<String, ArrayList<Integer>> entry : item.positions.entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    documentFrequency++;
                }
            }
        }

        return documentFrequency;
    }

    public double computeIDF(String term) {    //giving wrong output
        this.term = term;
        int documentFrequency = 0;
        for (HashMap.Entry<String, IndexData> entry : index.entrySet()) {
            IndexData item = entry.getValue();
            if (item.positions.containsKey(term)) {
                documentFrequency++;
            }
        }
        return Math.log((double) totalDocuments / (double) (documentFrequency + 1));
    }


    public double computeIDF2(String term) {    //testing -giving wrong output


        this.term = term;
        int documentFrequency = 0;

        // Assuming 'term' is the key in the 'index' map
        if (index.containsKey(term)) {
            IndexData item = index.get(term);

            // Check if the term is present in any document in the 'positions' map
            for (HashMap.Entry<String, ArrayList<Integer>> entry : item.positions.entrySet()) {
                // You might need to adjust this loop depending on the structure of your IndexData class
                if (!entry.getValue().isEmpty()) {
                    documentFrequency++;
                }
            }
        }

        System.out.println( "Document Frequency: " + documentFrequency);
        System.out.println( "Total Documents: " + totalDocuments);

        // Ensure documentFrequency is greater than 0 before computing the logarithm
        if (documentFrequency > 0) {
            //double temp = (double) totalDocuments / (double) documentFrequency;
            double result = Math.log(( (double) totalDocuments /(documentFrequency + 1)))+1;

            System.out.println("Computed IDF: " + result);
            return result;
        }

        // If documentFrequency is zero, return a default value
        System.out.println("Document Frequency is zero, returning default value.");
        return 0.98989898;
    }




    void computeTFIDFMatrix(String[] wordsArray, IndexData[] docArray, VectorSpace calc) {
        HashMap<String, Double> queryTFIDF = new HashMap<>();
        double queryLength = 0.0;

        for (String term : wordsArray) {
            double idf = calc.computeIDF(term);

            for (int i = 0; i < docArray.length; i++) {
                IndexData docName = docArray[i];
                double tf = calc.computeTF(term,docName);
                double tfidf = tf * idf;

                if (docName.equals(docArray[0])) { // Assuming the query document is the first element in docArray
                    queryTFIDF.put(term, tfidf);
                    queryLength += Math.pow(tfidf, 2);
                }
            }
        }

        queryLength = Math.sqrt(queryLength);

        // Compute similarity between query and documents
        for (int i = 0; i < docArray.length; i++) {
            IndexData docName = docArray[i];
            if (!docName.equals(docArray[0])) { // Assuming the query document is the first element in docArray
                double docLength = 0.0;

                for (HashMap.Entry<String, Double> queryEntry : queryTFIDF.entrySet()) {
                    String queryTerm = queryEntry.getKey();
                    double queryTFIDFValue = queryEntry.getValue();

                    double tf = calc.computeTF(queryTerm, docName);
                    double idf = calc.computeIDF(queryTerm);
                    double tfidf = tf * idf;

                    double docTFIDFValue = tfidf;

                    docLength += Math.pow(docTFIDFValue, 2);
                }

                docLength = Math.sqrt(docLength);
                double similarity = computeCosineSimilarity(queryTFIDF, queryLength, docLength);
                System.out.println("Query-Doc Similarity: " + docName + " | Similarity: " + similarity);
            }
        }
    }
    double computeDocumentLength(String doc){   //giving wrong output
        double docLength = 0.0;
        for (HashMap.Entry<String, IndexData> entry : index.entrySet()) {
            IndexData item = entry.getValue();
            if (item.positions.containsKey(doc)) {
                ArrayList<Integer> positions = item.positions.get(doc);
                double tf = (double) positions.size() / (double) item.tFrequency;
                double idf = computeIDF(doc);
                double tfidf = tf * idf;
                docLength += Math.pow(tfidf, 2);
            }
            }
        return Math.sqrt(docLength);

        }
    double computeDocumentLength2(String doc) {    //for testing
        double docLength = 0.0;

        // Compute IDF for the document once
        double idf = computeIDF(doc);

        for (HashMap.Entry<String, IndexData> entry : index.entrySet()) {
            IndexData item = entry.getValue();

            if (item.positions.containsKey(doc)) {
                ArrayList<Integer> positions = item.positions.get(doc);

                // Check for null positions
                if (positions != null) {
                    double tf = (double) positions.size() / (double) item.tFrequency;
                    double tfidf = tf * idf;
                    docLength += Math.pow(tfidf, 2);
                }
            }
        }

        return Math.sqrt(docLength);
    }

    double computeDocumentNormalizedTFIDF(IndexData item, HashMap<String, Double> queryTFIDF, HashMap<String, Double> idfValues, double docLength) {
        double normalizedTFIDF = 0.0;

        for (HashMap.Entry<String, Double> queryEntry : queryTFIDF.entrySet()) {
            String queryTerm = queryEntry.getKey();
            double queryTFIDFValue = queryEntry.getValue();

            if (item.positions.containsKey(queryTerm)) {
                ArrayList<Integer> queryPositions = item.positions.get(queryTerm);
                double tf = (double) queryPositions.size() / (double) item.tFrequency;
                double idf = idfValues.get(queryTerm);
                double tfidf = tf * idf;

                normalizedTFIDF += tfidf;
            }
        }

        return normalizedTFIDF / docLength;
    }

    double computeCosineSimilarity(HashMap<String, Double> queryTFIDF, double queryLength, double docLength) {
        double dotProduct = 0.0;

        for (HashMap.Entry<String, Double> queryEntry : queryTFIDF.entrySet()) {
            String term = queryEntry.getKey();
            double queryTFIDFValue = queryEntry.getValue();

            IndexData item = index.get(term);
            if (item.positions.containsKey("query")) {
                ArrayList<Integer> queryPositions = item.positions.get("query");
                double tf = (double) queryPositions.size() / (double) item.tFrequency;
                double idf = computeIDF(term);
                double tfidf = tf * idf;

                double docTFIDFValue = tfidf;

                dotProduct += queryTFIDFValue * docTFIDFValue;
            }
        }

        return dotProduct / (queryLength * docLength);
    }
}
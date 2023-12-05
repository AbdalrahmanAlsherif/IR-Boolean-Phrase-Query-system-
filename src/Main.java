import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        TablePrinter st1 = new TablePrinter();
        st1.setShowVerticalLines(true);
        TablePrinter st2 = new TablePrinter();
        st2.setShowVerticalLines(true);
        TablePrinter st3 = new TablePrinter();
        st3.setShowVerticalLines(true);
        TablePrinter st4 = new TablePrinter();
        st4.setShowVerticalLines(true);
        TablePrinter st5 = new TablePrinter();
        st5.setShowVerticalLines(true);
        TablePrinter st6 = new TablePrinter();
        st6.setShowVerticalLines(true);


        PreQueryHandler posCalc = new PreQueryHandler("Docs");
        HashMap<String, IndexData> positionalIndex = posCalc.positionalIndex;
        VectorSpace calc = new VectorSpace(posCalc.positionalIndex);
        for (IndexData token:posCalc.positionalIndex.values()){
            System.out.println(token.token);
        }
        String[] wordsArray = {
                "antony", "brutus", "caeser", "calpurnia", "cleopatra",
                "mercy", "worse", "angel", "fool", "fear", "in", "rush",
                "to", "tread", "where"
        };


        ArrayList<String> docArray = posCalc.documents;

        //+++++++Term frequency++++++++     //DONE -test pending-
        System.out.println("=====Term frequency=====");
        st1.setHeaders("   ","1.txt", "2.txt","3.txt","4.txt","5.txt","6.txt","7.txt","8.txt","9.txt","10.txt");
        for (int i = 0; i < posCalc.positionalIndex.size(); i++) {
            // Create a new row for each word
            //replace each docArray with getPositions(wordsArray[i],docArray[//index of doc])
            st1.addRow(posCalc.tokens.get(i), String.valueOf(posCalc.getPositions(positionalIndex.get(posCalc.tokens.get(i)).token,docArray.get(0))),
                    String.valueOf(posCalc.getPositions(positionalIndex.get((posCalc.tokens.get(i))).token,docArray.get(1))),
                    String.valueOf(posCalc.getPositions(positionalIndex.get((posCalc.tokens.get(i))).token,docArray.get(2))),
                    String.valueOf(posCalc.getPositions(positionalIndex.get((posCalc.tokens.get(i))).token,docArray.get(3))),
                    String.valueOf(posCalc.getPositions(positionalIndex.get((posCalc.tokens.get(i))).token,docArray.get(4))),
                    String.valueOf(posCalc.getPositions(positionalIndex.get((posCalc.tokens.get(i))).token,docArray.get(5))),
                    String.valueOf(posCalc.getPositions(positionalIndex.get((posCalc.tokens.get(i))).token,docArray.get(6))),
                    String.valueOf(posCalc.getPositions(positionalIndex.get((posCalc.tokens.get(i))).token,docArray.get(7))),
                    String.valueOf(posCalc.getPositions(positionalIndex.get((posCalc.tokens.get(i))).token,docArray.get(8))),
                    String.valueOf(posCalc.getPositions(positionalIndex.get((posCalc.tokens.get(i))).token,docArray.get(9))));
        }
//        prints the table
        st1.print();

//        //+++++++ w tf(1+ log tf) ++++++++ //NOT Complete
//        System.out.println("w tf(1+ log tf)");
//        st2.setHeaders("   ","d1", "d2","d3","d4","d5","d6","d7","d8","d9","d10");
//        for (int i = 0; i < wordsArray.length; i++) {
//            // Create a new row for each word
//            //
//            st2.addRow(wordsArray[i], calc.computeTF(wordsArray[i],??), docArray[1], docArray[2], docArray[3], docArray[4], docArray[5], docArray[6], docArray[7], docArray[8], docArray[9]);
//        }
//        //prints the table
//        st2.print();


        //+++++++ df  &   idf +++++    //DONE-tests pending-   //df works and right calc but wrong arrangment,idf works but wrong calculations
        st3.setHeaders("   ","df","idf");
        for (int i = 0; i < wordsArray.length; i++) {
            // Create a new row for each word
            //
            st3.addRow(wordsArray[i], String.valueOf(calc.computeDF2(positionalIndex.get(posCalc.tokens.get(i)).token)), String.valueOf(calc.computeIDF(positionalIndex.get(posCalc.tokens.get(i)).token)));
        }
        //prints table
        st3.print();
        //+++++ tf*idf +++++
//        System.out.println("===== tf*idf =====");
//        st4.setHeaders("   ","d1", "d2","d3","d4","d5","d6","d7","d8","d9","d10");
//        for (int i = 0; i < wordsArray.length; i++) {
//            // Create a new row for each word
//            //replace each docArray with ___
//            st4.addRow(wordsArray[i],docArray[0], docArray[1], docArray[2], docArray[3], docArray[4], docArray[5], docArray[6], docArray[7], docArray[8], docArray[9]);
//        }
//        //prints the table
//        st4.print();

        //+++++++ d length +++++     //DONE-test pending-   //works but wrong calculations
        st5.setHeaders("document","length");
        for (int i = 0; i < docArray.size(); i++) {
            // Create a new row for each word
            //replace ? with ___
            st5.addRow(docArray.get(i), String.valueOf(calc.computeDocumentLength(docArray.get(i))));
        }
        //prints the table
        st5.print();

//        //=====normalized tf.idf =====
//        System.out.println("=====normalized tf.idf=====");
//        st6.setHeaders("   ","d1", "d2","d3","d4","d5","d6","d7","d8","d9","d10");
//        for (int i = 0; i < wordsArray.length; i++) {
//            // Create a new row for each word
//            //replace each docArray with _____
//            st6.addRow(wordsArray[i],docArray[0], docArray[1], docArray[2], docArray[3], docArray[4], docArray[5], docArray[6], docArray[7], docArray[8], docArray[9]);
//        }
//        //prints the table
//        st6.print();
    }
}

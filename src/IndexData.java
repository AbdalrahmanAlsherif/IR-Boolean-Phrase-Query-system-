import java.util.ArrayList;
import java.util.HashMap;

public class IndexData {
    String token;
    HashMap<String, ArrayList<Integer>> positions;
    int tFrequency;

    public IndexData(String token, String docName, int position) {
        this.token = token;
        tFrequency = 1;
        positions = new HashMap<>();
        ArrayList<Integer> posArr = new ArrayList<>();
        positions.put(docName, posArr);
        posArr.add(position);
    }

    public void addTokenEntry(String docName, Integer position) {
        if (!positions.containsKey(docName)) {
            positions.put(docName, new ArrayList<>());
        }
        ArrayList<Integer> dPositions = positions.get(docName);
        dPositions.add(position);
        tFrequency++;
    }
}







import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

public class Rebuilder {

    public void Build(double[][] score) {
        try {
            Score[] aggScore = new Score[score.length];
            for (int i = 0; i < score.length; i++) {
                double temp = 0;
                for (int j = 0; j < score[0].length; j++) {
                    temp += score[i][j];
                }
                aggScore[i] = new Score(i,temp);
            }
            Arrays.sort(aggScore);
//            for(Score s: aggScore){
//                System.out.print(s.toString()+", ");
//            }
            int toPrint = (int)Math.ceil(aggScore.length/3)+1;
            int [] printIndex = new int[toPrint];
            for(int c= 0; c<printIndex.length;c++){
                printIndex[c] = aggScore[c].index;
            }
            Arrays.sort(printIndex);
            String[] text = readText().split("।");
            for(int c = 0; c<printIndex.length; c++){
                System.out.println(text[printIndex[c]]);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String readText(){
        try{
            BufferedReader inputFileReader = new BufferedReader(new FileReader(new File(".\\input.txt")));
            String line = "";
            String ret = "";
            while((line = inputFileReader.readLine())!=null){
                ret+=line;
            }
            return ret;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

class Score implements Comparable<Score> {
    int index;
    double score;
    Score(int i, double s){
        index = i;
        score = s;
    }
    @Override
    public int compareTo(Score o) {
        if (o.score < this.score) {
            return -1;
        } else {
            return +1;
        }
    }
    public String toString(){
        return score+"";
    }
}

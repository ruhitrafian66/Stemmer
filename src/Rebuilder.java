import com.opencsv.CSVReader;

import java.io.FileWriter;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

public class Rebuilder {

    public void AggregateOutput(double[][] score) {
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
            for(Score s: aggScore){
//                System.out.print(s.toString()+", ");
            }
            int toPrint = (int)Math.ceil(aggScore.length/3);
            int [] printIndex = new int[toPrint];
            for(int c= 0; c<printIndex.length;c++){
                printIndex[c] = aggScore[c].index;
            }
            Arrays.sort(printIndex);
            String[] text = readText().split("।");
            String output = "";
            for(int c = 0; c<printIndex.length; c++){
                output = text[printIndex[c]];
//                System.out.println(text[printIndex[c]]);
            }
            FileWriter f = new FileWriter(new File(".\\output.txt"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FCMOutput() {
        try {
            System.out.println("printing summary");
            Scanner sc = new Scanner(new File("clusters.csv"));
            BufferedReader br = new BufferedReader(new FileReader("clusters.csv"));
            String line;
            String []text = readText().split("। ");
            int iteration = 0;
            int clusterSelect = 1;
            while ((line = br.readLine()) != null) {
                if (iteration == 0) {
                    iteration++;
                } else {
                    String[] cols = line.split(",");
                    if (cols[0].equals("") || cols[1].equals("")) {
                        break;
                    } else {
                        if (iteration == 1) {
                            if (Integer.parseInt(cols[0]) == 0) {
                                clusterSelect = 0;
                            } else {
                                clusterSelect = 1;
                            }
                            iteration++;
                        }
                    }
                    int temp = Integer.parseInt(cols[clusterSelect]);
                    System.out.println(text[temp]);
                }
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
        } else if(o.score>this.score){
            return +1;
        }else return 0;
    }
    public String toString(){
        return score+"";
    }
}
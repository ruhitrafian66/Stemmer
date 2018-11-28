import com.opencsv.CSVWriter;
import java.io.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Stemmer {
    public static String[] pr;
    public static ArrayList<Sentence> sen;
    public static MyArrayList<Word> word;
    public static ArrayList<Paragraph> para;

    public static void main(String[] args) {

        String text = ActualStemmer.StemmedText();

        sen = new ArrayList<>();
        word = new MyArrayList<>();
        para = new ArrayList<>();

        //tokenize, create and populate arraylists
        String[] p = text.split("\n");
        int totalPos = 0;
        for (int i = 0; i < p.length; i++) {
            if (!p[i].equals(null)) {
                String[] s = p[i].split("।");
                para.add(new Paragraph(i, s.length, p[i]));
                for (int j = 0; j < s.length; j++) {
                    if (!(s[j].equals(" "))) {
                        boolean tsFlag = false;
                        if (j == 0) {
                            tsFlag = true;
                        }
                        String[] w = s[j].split(" ");
                        sen.add(new Sentence(j, s[j], tsFlag, w.length, i + 1, totalPos));
                        totalPos++;
                        for (int k = 0; k < w.length; k++) {
                            if (!(word.contains(w[k]))) {
                                word.add(new Word(i, w[k]));
                            } else {
                                word.iterate(w[k]);
                            }
                        }

                    }
                }
            }
        }
//        for (int c = 0; c < sen.size(); c++) {
//            Sentence se = sen.get(c);
//            System.out.print(se.pos + " /" + se.truePos + ": " + se.toString());
//            System.out.println();
//        }
        Rebuilder r = new Rebuilder();
        r.Build(Finalize());
    }

    public static void EvaluateTFIDF() {
        double maxIDF = 0;
        int cnt = 1;
        for (Sentence s : sen) {
            String[] w = s.text.split(" ");
            double score = 0;
            for (String i : w) {
                int freq = word.lookUp(i);
                double tf = freq * Math.log(sen.size() / freq);
                score += tf;
            }
            s.tfscore = score;
            if (score > maxIDF) {
                maxIDF = score;
            }
        }
        cnt = 1;
        for (Sentence s : sen) {
            s.tfscore = s.tfscore / maxIDF;
        }
    }

    //evaluates length relative scores of each sentence
    public static void EvaluateLengthScore() {
        int avgLength = 0;
        for (int c = 0; c < sen.size(); c++) {
            avgLength += sen.get(c).len;
        }
        avgLength /= sen.size();
        int[] score = new int[sen.size()];
        for (int c = 0; c < sen.size(); c++) {
            score[c] = sen.get(c).len / avgLength;
            if (score[c] <= 1) {
                sen.get(c).lenScore = score[c];
            } else if (score[c] <= 2) {
                sen.get(c).lenScore = 2 - score[c];
            } else if (score[c] > 2) {
                sen.get(c).lenScore = 0;
            }
        }
    }

    public static void EvaluateCueScore(){
        File cue_words = new File("cue_words.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(cue_words);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
        while (sc.hasNextLine()) {
            String cue = sc.nextLine();
            for (int c = 0; c < sen.size(); c++) {
                String[] temp = sen.get(c).text.split(" ");
                for (int i = 0; i < temp.length; i++) {
                    if (temp[i].equals(cue)) {
                        sen.get(c).cueScore++;
                    }
                }
            }
        }
        double maxScore = 0;
        for (Sentence s : sen) {
            if (s.cueScore > maxScore) {
                maxScore = s.cueScore;
            }
        }
        for (Sentence s : sen) {
            s.cueScore = s.cueScore / maxScore;
        }

    }

    public static void EvaluateTopicSentenceScore() {
        ArrayList<String> tsWords = new ArrayList<>();
        for (Sentence s : sen) {
            if (s.ts) {
                String[] temp = s.text.split(" ");
                tsWords.addAll(Arrays.asList(temp));
            }
        }
        for (Sentence s : sen) {
            String[] temp = s.text.split(" ");
            for (String t : temp) {
                if (tsWords.contains(t)) {
                    s.topicScore++;

                }
            }
        }
        double max = 0;
        for (Sentence s : sen) {
            if (s.topicScore > max) {
                max = s.topicScore;
            }
        }
        for (Sentence s : sen) {
            s.topicScore = s.topicScore / max;
        }
    }

    public static void EvaluateNumValScore() {
        CharSequence[] ch = new CharSequence[10];
        //"0, ১, ২, ৩, ৪, ৫, ৬, ৭, ৮, ৯"
        ch[0] = new StringBuffer("০");
        ch[1] = new StringBuffer("১");
        ch[2] = new StringBuffer("২");
        ch[3] = new StringBuffer("৩");
        ch[4] = new StringBuffer("৪");
        ch[5] = new StringBuffer("৫");
        ch[6] = new StringBuffer("৬");
        ch[7] = new StringBuffer("৭");
        ch[8] = new StringBuffer("৮");
        ch[9] = new StringBuffer("৯");
        for (int c = 0; c < sen.size(); c++) {
            String[] s = sen.get(c).text.split(" ");
            for (int c1 = 0; c1 < s.length; c1++) {
                for (int c2 = 0; c2 < ch.length; c2++) {
                    if (s[c1].contains(ch[c2]))
                        sen.get(c).numScore++;
                }
            }
        }
        for (Sentence s : sen) {
            s.numScore = s.numScore / s.len;
        }
    }

    public static void EvaluatePositionScore() {
        for (Paragraph pr : para) {

        }
    }

//        public static void EvaluatePositionScore () {
//            for (Paragraph pr : para) {
//                for (Sentence sc : sen) {
//                    if (sc.paraNo == pr.paraNo && sc.pos <= Math.ceil(0.1 * pr.noOfSentences)) {
//                        sc.posScore += 1;
//                    } else if (sc.paraNo == pr.paraNo && sc.pos >= Math.floor(0.9 * pr.noOfSentences)) {
//                        sc.posScore += 1;
//                    } else if (sc.paraNo == pr.paraNo) {
//                        sc.posScore += 0;
//                    }
//                }
//            }
//        }


    //    double array is array of scores for each sentence. first braces mean sentence index, second braces mean score
    public static double[][] Finalize() {
        //
        EvaluateTFIDF();
        EvaluateLengthScore();
        EvaluateCueScore();
        EvaluateTopicSentenceScore();
        EvaluatePositionScore();
        EvaluateNumValScore();
        try {
            File file = new File(".\\output.csv");
            FileWriter outputfile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputfile);
            String[] label = {"score1", "score2", "score3", "score4", "score5", "score6"};
            writer.writeNext(label);
            double[][] score = new double[sen.size()][6];
            //this array is initiated for output only, optimize it if possible
            double[][] outputScore = new double[sen.size()][6];
            int i = 0;
            int j = 0;
            DecimalFormat df2 = new DecimalFormat("#.####");
            int c = 0;
            for (Sentence s : sen) {
                outputScore[j][0] = s.tfscore;
                outputScore[j][1] = s.numScore;
                outputScore[j][2] = s.lenScore;
                outputScore[j][3] = s.cueScore;
                outputScore[j][4] = s.topicScore;
                outputScore[j][5] = s.posScore;
                for (int x=0; x<6; x++) {
                    Double q = outputScore[j][x];
                    //System.out.println(q);
                    if(q.equals(Double.NaN)) {
                        outputScore[j][x]=0;
                    }
                }
                String []data = {df2.format(outputScore[j][0])+"", df2.format(outputScore[j][1])+"", outputScore[j][2]+"", outputScore[j][3]+"", df2.format(outputScore[j][4])+"", outputScore[j][5]+""};

                writer.writeNext(data);
                score[i][0] = s.tfscore;
                score[i][1] = s.numScore;
                score[i][2] = s.lenScore;
                score[i][3] = s.cueScore;
                score[i][4] = s.topicScore;
                score[i++][5] = s.posScore;
                j++;

            }
            writer.close();
            return score;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
//        for (Sentence s : sen) {
////            System.out.println(s.text);
//            System.out.println(df2.format(s.tfscore) + "     " + df2.format(s.posScore) + "      " + df2.format(s.lenScore)
//                    + "      " + df2.format(s.cueScore) + "      " + df2.format(s.numScore) + "      " + df2.format(s.topicScore));
//        }
    }


}
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class Stemmer {
    public static String[] pr;
    public static ArrayList<Sentence> sen;
    public static MyArrayList<Word> word;
    public static ArrayList<Paragraph> para;

    public static void main(String[] args) {

        String text = ActualStemmer.StemmedText();

        //splitting sentences
        //String[] st = text.replaceAll("(\r\n|\r|\n)+", "").split("।"); //modified for multiple paras

        //spliting paragraphs   Rule
        pr = text.replaceAll("(\r\n|\r|\n)+", "\n").split("\n");

        //main arraylists
        sen = new ArrayList<>();
        word = new MyArrayList<>();
        para = new ArrayList<>();

        //tokenize, create and populate arraylists

        int senNoDoc = 0;
        for (int i = 0; i < pr.length; ++i) {
            //splitting sentences
            String[] st = pr[i].replaceAll("(\r\n|\r|\n)+", "").split("।"); //modified for multiple paras
            para.add(new Paragraph(st, i + 1, pr[i]));
            for (int c = 0; c < st.length; c++) {
                String[] w = st[c].split(" ");
                sen.add(new Sentence(c, st[c], false, w.length, i + 1, senNoDoc));
                ++senNoDoc;
                for (int c1 = 0; c1 < w.length; c1++) {
                    if (!(word.contains(w[c1]))) {
                        word.add(new Word(c, w[c1]));
                    } else {
                        word.iterate(w[c1]);
                    }
                }
            }
        }
        Rebuilder r = new Rebuilder();
        r.Build( Finalize());
    }

    public static void printTest() {
        for (Sentence s : sen) {
            System.out.println();
        }
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

    public static void EvaluateCueScore() {
        File cue_words = new File("cue_words.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(cue_words);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }

        for (int c = 0; c < sen.size(); c++) {
            ///avgLength += sen.get(c).len;
        }

        while (sc.hasNextLine()) {
            String cue = sc.nextLine();
            for (int c = 0; c < sen.size(); c++) {
                String[] temp = sen.get(c).text.split(" ");
                for (int i = 0; i < temp.length; i++) {
                    if (temp[i].equals(cue)) {

//                        System.out.println(c + " " + cue);

                        sen.get(c).cueScore++;
                    }
                }
            }
        }

//        for (int c = 0; c < sen.size(); c++) {
//            System.out.println("cue score" + sen.get(c).cueScore);
//            ///avgLength += sen.get(c).len;
//        }


    }

    public static void EvaluateTopicSentenceScore() {
        for (int x = 1; x <= pr.length; x++) {
            if (x == 1) {
                String[] passage_topic = sen.get(0).text.split(" ");
                for (int y = 0; y < passage_topic.length; y++) {
                    for (int c = 0; c < sen.size(); c++) {
                        String sc = sen.get(c).text;
                        String[] temp = Sentence.createWords(sc);
                        for (int i = 0; i < temp.length; i++) {
                            if (temp[i].equals(passage_topic[y])) {

                                sen.get(c).topicScore++;
                                //System.out.println("topic score for the very first line" + " " + sen.get(c).pos+" "+ sen.get(c).topicScore);
                            }
                        }
                    }
                }
            } else {
                String paragraph = para.get(x - 1).para; //fetching the paragraph
                String[] sentences = Paragraph.createSentences(paragraph); //breaking the paragragh into sentences
                //System.out.println(sentences[0]);
                String[] para_topic = Sentence.createWords(sentences[0]); //getting the paragraph topic sentence
                for (int k = 0; k < sentences.length; k++) {
                    String[] words = Sentence.createWords(sentences[k]); //test sentence broken into words
                    for (int i = 0; i < para_topic.length; i++) {
                        for (int j = 0; j < words.length; j++) {
                            if (words[j].equals(para_topic[i])) {
                                for (int z = 0; z < sen.size(); z++) {
                                    if (sen.get(z).text.equals(sentences[k])) {
                                        if (sen.get(z).paraNo == x) {
                                            //System.out.println(sen.get(z).text);
                                            //System.out.println(words[j]);
                                            sen.get(z).topicScore++;
                                            //System.out.println("Topic score updated: " + sen.get(z).senNoDoc + " " + sen.get(z).topicScore);
                                        }
                                    }
                                }
                                //Sentence temp= MyArrayList.getSentence(sentences[k]);
                                //temp.topicScore++;

                            }
                        }
                    }

                }

            }
            for(Sentence s: sen){
                s.topicScore = s.topicScore/s.len;
            }
        }


    }

    public static void EvaluateNumValScore() {
        CharSequence[] ch = new CharSequence[10];
        ch[0] = new StringBuffer("0");
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
            for (Sentence sc : sen) {
                if (sc.paraNo == pr.paraNo && sc.pos <= Math.ceil(0.1 * pr.noOfSentences)) {
                    sc.posScore += 1;
                } else if (sc.paraNo == pr.paraNo && sc.pos >= Math.floor(0.9 * pr.noOfSentences)) {
                    sc.posScore += 1;
                } else if (sc.paraNo == pr.paraNo) {
                    sc.posScore += 0;
                }
            }
        }
    }

    public static void evaluate() {

    }

    //double array is array of scores for each sentence. first braces mean sentence index, second braces mean score
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
            String[] label = {"score1", "score2","score3","score4","score5","score6"};
            writer.writeNext(label);
        double[][] score = new double[sen.size()][6];
            int i = 0;
        DecimalFormat df2 = new DecimalFormat("#.####");
            int c = 0;
            for (Sentence s : sen) {
                String []data = {df2.format(s.tfscore)+"", df2.format(s.numScore)+"", s.lenScore+"", s.cueScore+"", df2.format(s.topicScore)+"", s.posScore+""};
                writer.writeNext(data);
            score[i][0] = s.tfscore;
            score[i][1] = s.numScore;
            score[i][2] = s.lenScore;
            score[i][3] = s.cueScore;
            score[i][4] = s.topicScore;
            score[i++][5] = s.posScore;
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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class Stemmer {

    public static void main(String[] args) {
        //single paragraphs
//        String text = "সুতরাং একাদশ জাতী সংসদ নির্বাচন 13 তফসিল ঘোষণ পর মনোনয়ন ফরম বিক্রির প্রথম দিন দেশ প্রধান " +
//                " দু যেহেতু রাজনৈতিক দল জাতী কার্যাল ভিন্ন চিত্র দেখা গেছে। উৎসবমুখর পরিবাশ মনোনয়ন ফরম বিক্রি হ আওয়ামী লীগ" +
//                " কার্যালয়ে। কিন্তু বিএনপি কার্যাল ন কোনো নির্বাচনী তৎপরতা।";
        //multiple paragraphs = 3
        String text = "নির্বাচনে দলের সম্ভাব্য প্রার্থী ও কর্মী-সমর্থকদের আচরণবিধি মেনে চলাতে কঠোর হওয়ার জন্য আইনশৃঙ্খলা রক্ষাকারী বাহিনীকে চিঠি" +
                " দিয়েছে নির্বাচন কমিশন (ইসি)। আজ মঙ্গলবার এ–সংক্রান্ত চিঠি পুলিশের মহাপরিদর্শক (আইজিপি) বরাবর পাঠিয়েছে নির্বাচন কমিশন।\n" +
                "\n" +
                "নির্বাচন কমিশন চিঠিটি এমন সময়ে দিল যখন আচরণবিধি ভঙ্গ করে রাজধানীতে বিএনপি অফিসের সামনে হাজার হাজার নেতা-কর্মী জড়ো " +
                "হতে শুরু করেছেন এবং আওয়ামী লীগের নেতা-কর্মীরা নানাভাবে আচরণবিধি লঙ্ঘন করে দলীয় মনোনয়ন ফরম সংগ্রহের কাজ শেষ হয়েছে।\n" +
                "\n" +
                "এর আগে ১০ নভেম্বর মনোনয়ন ফরম সংগ্রহ করাকে কেন্দ্র করে রাজধানীর মোহাম্মদপুরে আওয়ামী লীগের দুই পক্ষের সংঘর্ষের ঘটনায় দুই " +
                "কিশোরের প্রাণহানি ঘটে। তখন ইসির ভূমিকা ছিল অনেকটাই নির্বিকার।";

        //splitting sentences
        String[] st = text.replaceAll("(\r\n|\r|\n)+", "").split("।"); //modified for multiple paras

        //spliting paragraphs
        String[] pr = text.replaceAll("(\r\n|\r|\n)+", "\n").split("\n");

        //main arraylists
        ArrayList<Sentence> sen = new ArrayList<>();
        MyArrayList<Word> word = new MyArrayList<>();

        //tokenize, create and populate arraylists
        int paraPos = 0;
        for(int i = 0; i<pr.length ; ++i){
                for (int c = 0; c < st.length; c++) {
                String[] w = st[c].split(" ");
                sen.add(new Sentence(c, st[c], false, w.length,i+1, paraPos));
                ++paraPos;

                for (int c1 = 0; c1 < w.length; c1++) {
                    if (!(word.contains(w[c1]))) {
                        word.add(new Word(c, w[c1]));
                    } else {
                        word.iterate(w[c1]);
                    }
                }
            }
        }

        EvaluateLengthScore(sen);
//        EvaluateCueScore(sen);
    }

    public static void EvaluateTFIDF(ArrayList<Sentence> sen, MyArrayList<Word> word){
        
    }


    //evaluates length relative scores of each sentence
    public static void EvaluateLengthScore(ArrayList<Sentence> sen) {
        int avgLength = 0;
        for (int c = 0; c < sen.size(); c++) {
            System.out.println(sen.get(c).len);
            avgLength += sen.get(c).len;
        }
        System.out.println(avgLength);
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
            System.out.println(sen.get(c).lenScore);
        }
    }

    public static void EvaluateCueScore(ArrayList<Sentence> sen) {
        File cue_words = new File("cue_words.txt");
        Scanner sc = null;
        try {
            sc = new Scanner(cue_words);
        } catch (FileNotFoundException e) {
            System.out.println("file not found");
        }
        for (int c = 0; c < sen.size(); c++) {
            System.out.println(sen.get(c).text);
            ///avgLength += sen.get(c).len;
        }
        while (sc.hasNextLine()) {
            String cue = sc.nextLine();
            for (int c = 0; c < sen.size(); c++) {
                String[] temp = sen.get(c).text.split(" ");
                for (int i = 0; i < temp.length; i++) {
                    if (temp[i].equals(cue)) {
                        System.out.println(c + " " + cue);
                        sen.get(c).cueScore++;
                    }
                }
            }
            //if(sen.get(c).text.contains(cue))
            //{
            //    System.out.println(c +" " +cue);
            //    sen.get(c).cueScore++;
            //}
            //System.out.println(sen.get(c).text);
            ///avgLength += sen.get(c).len;
            //}
        }
        for (int c = 0; c < sen.size(); c++) {
            System.out.println("cue score" + sen.get(c).cueScore);
            ///avgLength += sen.get(c).len;
        }

    }

    public static void EvaluateNumValScore(ArrayList<Sentence> sen) {
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
    }

    public static void positionOfSentence(){

    }
}



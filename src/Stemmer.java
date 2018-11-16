import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.*;
import java.util.Scanner;

public class Stemmer {
    public static String[] pr;
    public static ArrayList<Sentence> sen;
    public static void main(String[] args) {
        //single paragraphs
//        String text = "সুতরাং একাদশ জাতী সংসদ নির্বাচন 13 তফসিল ঘোষণ পর মনোনয়ন ফরম বিক্রির প্রথম দিন দেশ প্রধান " +
//                " দু যেহেতু রাজনৈতিক দল জাতী কার্যাল ভিন্ন চিত্র দেখা গেছে। উৎসবমুখর পরিবাশ মনোনয়ন ফরম বিক্রি হ আওয়ামী লীগ" +
//                " কার্যালয়ে। কিন্তু বিএনপি কার্যাল ন কোনো নির্বাচনী তৎপরতা।";
        //multiple paragraphs = 3


        String text = "আদালত সূত্রে জানা গেছে, ২০০৭ সালের ৬ জানুয়ারি লাকসাম উপজেলার শ্রীয়াং বাজারে দোকান বন্ধ করে ক্ষুদ্র কাঁচামাল ব্যবসায়ী উত্তম দেবনাথ (২৭), পরীক্ষিত দেবনাথ (১২) ও পান ব্যবসায়ী বাচ্চু মিয়া (৩৫) ভ্যানে করে বাড়ি ফিরছিলেন। রাত ১২টার দিকে লাকসামের শ্রীয়াং ও রাজাপুর সড়কের বদিরপুকুরে পৌঁছালে দণ্ডপ্রাপ্ত আসামিরা ডাকাত পরিচয় দিয়ে মাত্র এক হাজার ৪০০ টাকার জন্য সড়কের পাশে ফসলি জমিতে নিয়ে তিনজনকে গলা কেটে হত্যা করে। নিহত উত্তম দেবনাথ (২৭), পরীক্ষিত দেবনাথ (১২) মনোহরগঞ্জ উপজেলা প্রতাপপুর গ্রামের মণিন্দ্র দেবনাথের ছেলে। এ ছাড়া বাচ্চু মিয়া লাকসাম উপজেলার জগতপুর গ্রামের সামছুল হকের ছেলে। \n" +

                "\n" +
                "আদালত সূত্রে আরও জানা গেছে, ওই বছরের ৭ জানুয়ারি নিহত বাচ্চুর ছোট ভাই কবির হোসেন বাদী হয়ে লাকসাম থানায় একটি হত্যা মামলা দায়ের করেন। পরবর্তীতে দীর্ঘ ১০ মাস তদন্ত শেষে লাকসাম থানা-পুলিশ পাঁচজনকে আসামি করে আদালতে একটি অভিযোগপত্র দাখিল করেন। এরপর দীর্ঘ সময় ধরে এ মামলার শুনানি হয়। বুধবার রায় দেওয়া হয়।\n" +
                "\n" +
                "এ মামলার রাষ্ট্রপক্ষের আইনজীবী ছিলেন মো. আবু তাহের এবং আসামি পক্ষের আইনজীবী ছিলেন নাঈমা সুলতানা মুন্নি। রায়ে সন্তোষ প্রকাশ করে বাদী কবির হোসেন প্রথম আলোকে বলেন, ‘আসামিদের দ্রুত গ্রেপ্তার করে সাজা কার্যকর করতে হবে। এর মাধ্যমে আইনের শাসন নিশ্চিত হলো। সব ধরনের সমঝোতা প্রস্তাব ফিরিয়ে দিয়ে ও হুমকি সহ্য করে এ রায় পেয়েছি।";

        //splitting sentences
        //String[] st = text.replaceAll("(\r\n|\r|\n)+", "").split("।"); //modified for multiple paras

        //spliting paragraphs
        pr = text.replaceAll("(\r\n|\r|\n)+", "\n").split("\n");

        //main arraylists
        //ArrayList<Sentence>
                sen = new ArrayList<>();
        MyArrayList<Word> word = new MyArrayList<>();
        ArrayList<Paragraph> para = new ArrayList<>();

        //tokenize, create and populate arraylists

        int senNoDoc = 0;
        for(int i = 0; i<pr.length ; ++i){
            //splitting sentences
            String[] st = pr[i].replaceAll("(\r\n|\r|\n)+", "").split("।"); //modified for multiple paras
            para.add(new Paragraph(st,i+1,pr[i]));
            for (int c = 0; c < st.length; c++) {
                String[] w = st[c].split(" ");
                sen.add(new Sentence(c, st[c], false, w.length,i+1, senNoDoc));
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


//        EvaluateLengthScore(sen);
//        EvaluateCueScore(sen);

//          positionOfSentence(sen,para);
        EvaluateTopicSentenceScore(sen, para);
//        EvaluateTFIDF(sen, word);
    }

//    public static void printTest(ArrayList<Sentence> sen, MyArrayList<Word> word){
//        System.out.println("Position    "+"NumScore     "+"PosScore    "+"CueScore     "+"LenScore      "+"TFScore      ");
//        for(Sentence s : sen){
//            System.out.println(s.pos+"      "+s.numScore+"      +"      "+"     "+s.cueScore+");
//        }
//    }


    public static void EvaluateTFIDF(ArrayList<Sentence> sen, MyArrayList<Word> word) {
        double maxIDF = 0;
        int cnt = 1;
        for (Sentence s : sen) {
            String[] w = s.text.split(" ");
            double score = 0;
            for (String i : w) {
                int freq = word.lookUp(i);
                double tf = freq*Math.log(sen.size()/freq);
                score+=tf;
            }
            s.tfscore = score;
            System.out.println(cnt++ +" "+score);
            if(score>maxIDF){
                maxIDF = score;
            }
        }
        cnt = 1;
        for(Sentence s: sen){
            s.tfscore = s.tfscore/maxIDF;
        }
    }


    //evaluates length relative scores of each sentence
    public static void EvaluateLengthScore(ArrayList<Sentence> sen) {
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

    public static void EvaluateCueScore(ArrayList<Sentence> sen) {
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

    public static void EvaluateTopicSentenceScore(ArrayList<Sentence> sen, ArrayList<Paragraph> para)
    {
        for(int x=1; x<=pr.length; x++)
        {
            if(x==1)
            {
                String[] passage_topic= sen.get(0).text.split(" ");
                for(int y= 0; y<passage_topic.length; y++) {
                    for (int c = 0; c < sen.size(); c++) {
                        String sc= sen.get(c).text;
                        String[] temp = Sentence.createWords(sc);
                        for (int i = 0; i < temp.length; i++) {
                            if (temp[i].equals(passage_topic[y])) {
                                //System.out.println(c + " " + cue);
                                sen.get(c).topicScore++;
                            }
                        }
                    }
                }
            }
            else{
                String paragraph = para.get(x-1).para; //fetching the paragraph
                String [] sentences = Paragraph.createSentences(paragraph);
                String [] para_topic = Sentence.createWords(sentences[0]);
                for(int x= 0; x<sentences.length; x++)
                {
                    String [] words= Sentence.createWords(sentences[x]);
                    for(int i=0; i<para_topic.length; i++)
                    {
                        for(int j=0; j<words.length; j++)
                        {
                            if(words[j].equals(para_topic[i]))
                            {
                                sen.get().topicScore++;
                            }
                        }
                    }

                }


            }

        }
        String sc= sen.get(1).text;
        String [] wor= Sentence.createWords(sc);
        for (int c = 0; c < wor.length; c++) {
            System.out.println("topic score sentence wise: "+ wor[c]);
            ///avgLength += sen.get(c).len;
        }
        for (int c = 0; c < sen.size(); c++) {
            System.out.println("topic score sentence wise: " + sen.get(c).topicScore +" "+ sen.get(c).senNoDoc);
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
        for (Sentence s : sen) {
            s.numScore = s.numScore / s.len;
        }
    }


    public static void positionOfSentence(ArrayList<Sentence> sen,ArrayList<Paragraph> para){
        for(Paragraph pr: para){
            for (Sentence sc : sen) {
                if (sc.paraNo == pr.paraNo && sc.pos <= Math.ceil(0.1 * pr.noOfSentences)) {
                    sc.posScore += 10;
                }else if (sc.paraNo == pr.paraNo&& sc.pos >= Math.floor(0.9 * pr.noOfSentences)) {
                    sc.posScore += 10;
                }else if(sc.paraNo == pr.paraNo){
                    sc.posScore += 1;
                }
            }



        }

        for (Sentence sc : sen) {
            System.out.println(sc.text);
            System.out.println(sc.senNoDoc + " -> " + sc.posScore);
        }
    }
}
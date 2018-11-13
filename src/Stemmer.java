import java.util.ArrayList;

public class Stemmer {

    public static void main(String[] args) {
        String text = "একাদশ জাতী সংসদ নির্বাচন 13 তফসিল ঘোষণ পর মনোনয়ন ফরম বিক্রির প্রথম দিন দেশ প্রধান" +
                " দু রাজনৈতিক দল জাতী ৩ কার্যাল ভিন্ন চিত্র দেখা গেছে। উৎসবমুখর পরিবাশ মনোনয়ন ফরম বিক্রি হ আওয়ামী লীগ" +
                " কার্যালয়ে। কিন্তু বিএনপি ৩৪৪ কার্যাল ন কোনো ৪৪ ৩ নির্বাচনী তৎপরতা।";

        //splitting sentences
        String[] st = text.split("।");

        //main arraylists
        ArrayList<Sentence> sen = new ArrayList<>();
        MyArrayList<Word> word = new MyArrayList<>();

        //tokenize, create and populate arraylists
        for (int c = 0; c < st.length; c++) {
            String[] w = st[c].split(" ");
            sen.add(new Sentence(c, st[c], false, w.length));

            for (int c1 = 0; c1 < w.length; c1++) {
                if (!(word.contains(w[c1]))) {
                    word.add(new Word(c, w[c1]));
                } else {
                    word.iterate(w[c1]);
                }
            }
        }
//        EvaluateLengthScore(sen);
        EvaluateNumValScore(sen, word);
        for(int c = 0; c<sen.size();c++){
            System.out.println(sen.get(c).text+" "+sen.get(c).numScore);
        }
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

    public static void EvaluateNumValScore(ArrayList<Sentence> sen, MyArrayList<Word> word) {
//        char[] ch = {'০', '১', '২', '৩', '৪', '৫', '৬', '৭', '৮', '৯'};
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
        for(int c = 0; c<sen.size();c++){
            String []s = sen.get(c).text.split(" ");
            for(int c1 = 0; c1<s.length;c1++){
                for(int c2 = 0; c2<ch.length;c2++){
                    if(s[c1].contains(ch[c2]))
                        sen.get(c).numScore++;
                }
            }
        }


    }

}
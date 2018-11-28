import java.util.ArrayList;

public class Sentence {
    int pos;
    String text;
    int len;
    boolean ts = false;
    String [] words;
    ArrayList<Word> w= new ArrayList<>();
    double tfscore;
    int lenScore;
    double numScore;
    double topicScore;
    double posScore = 0;
    double cueScore;
    int paraNo;
    int truePos;

    public Sentence(int p, String st, boolean t, int l,int  paraNo, int truePos){
        pos = p;
        text = st;
        ts = t;
        len = l;
        this.paraNo = paraNo;
        this.truePos = truePos;
        String[] words = createWords(st);
    }
    public static String[] createWords(String s) {
        String[] words = s.split(" ");
        return words;
    }


    public String toString(){
        return text;
    }
    //overrides default equals method so that contains can be used to check if sentence already present in arraylist
    public boolean equals(Object o){
        if(o instanceof String){
            return text.equals(o);
        }
        if(o instanceof Sentence){
            return text.equals(((Sentence)o).text);
        }
        return false;
    }
}
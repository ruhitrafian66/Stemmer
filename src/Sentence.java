import java.util.ArrayList;

public class Sentence {
    int pos;
    String text;
    int len;
    boolean ts = false;
    ArrayList<Word> w= new ArrayList<>();

    int tfscore;
    int lenScore;
    int numScore;
    int topicScore;
    int posScore;
    int cueScore;
    int paraNo;
    int senNoForMultiPara;

    public Sentence(int p, String st, boolean t, int l,int  paraNo, int senNoForMultiPara){
        pos = p;
        text = st;
        ts = t;
        len = l;
        this.paraNo = paraNo;
        this.senNoForMultiPara = senNoForMultiPara;
        createWords();
    }
    public void createWords(){

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

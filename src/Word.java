import java.util.ArrayList;

public class Word {
    String text;
    ArrayList<Integer> pos = new MyArrayList<>();
    int score;
    int freq = 1;
    public Word(int p, String t){
        pos.add(p);
        text = t;
    }

    //overrides default equals method so that contains can be used to check if word already present in arraylist
    public boolean equals(Object o){
        if(o instanceof String){
            return text.equals(o);
        }
        if(o instanceof Word){
            return text.equals(((Word)o).text);
        }
        return false;
    }


    public String toString(){
        return text;
    }
}

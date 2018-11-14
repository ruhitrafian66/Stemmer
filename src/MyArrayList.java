import java.util.ArrayList;

public class MyArrayList<E> extends ArrayList<E> {


    public void iterate(String s){
        for(int c = 0;c<this.size();c++){
            Word w = (Word) this.get(c);
            if(w.text.equals(s)){
                w.freq++;
            }
        }
    }

    public boolean contains(String s){
        for(int c = 0;c<size();c++){
            Word w = (Word)this.get(c);
            if(s.equals(w.toString())){
                return true;
            }
        }
        return false;
    }
    public int lookUp(String s){
        for(int c = 0;c<size();c++){
            Word w = (Word)this.get(c);
            if(s.equals(w.toString())){
                return w.freq;
            }
        }
        return 0;
    }

    public String toString(){
        String ret = "";
        for(int c = 0;c<this.size();c++){
            Word w = (Word)this.get(c);
            ret+= w.toString()+" : "+w.freq+" "+w.pos+"\n";
        }
        return ret;
    }
}

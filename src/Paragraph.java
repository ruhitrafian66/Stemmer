public class Paragraph {
    String[] text;
    int paraNo;
    int noOfSentences;
    String para;
    String [] sentences;

    public Paragraph(String[] tx,int paraNo,String para){
        text = tx;
        this.paraNo = paraNo;
        noOfSentences = tx.length ;
        this.para = para;
    }
    public static String[] createSentences(String s) {
        String[] sentences = s.split("।");
        return sentences;
    }
}

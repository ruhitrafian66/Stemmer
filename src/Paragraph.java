public class Paragraph {
    String[] text;
    int paraNo;
    int noOfSentences;

    public Paragraph(String[] tx,int paraNo){
        text = tx;
        this.paraNo = paraNo;
        noOfSentences = tx.length ;
    }
}

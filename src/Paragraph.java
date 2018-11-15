public class Paragraph {
    String[] text;
    int paraNo;
    int noOfSentences;
    String para;

    public Paragraph(String[] tx,int paraNo,String para){
        text = tx;
        this.paraNo = paraNo;
        noOfSentences = tx.length ;
        this.para = para;
    }
}

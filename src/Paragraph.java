public class Paragraph {
    int index;
    String text;
    int size;
    Paragraph(int i, int size ,String t){
        index = i;
        text = t;
        this.size = size;
    }

    public static String[] createSentences(String s) {
        String[] sentences = s.split("।");
        return sentences;
    }
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class ActualStemmer {
    /**
     * The program will be run using the following command:<p>
     * {@code Stemmer rulesfile inputfile}.
     */
    public static String StemmedText() {
        //reading files
        File stopFile = null;
        File inputFile = null;
        RuleFileParser parser = null;
        try {

            stopFile = new File("stopwords.txt");
            parser = new RuleFileParser("stem.rules");
            inputFile = new File("input.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //reading stopwords into arraylist stop
        ArrayList<String> stop = new ArrayList<>();
        String s;
        try {
            BufferedReader f = new BufferedReader(new FileReader(stopFile));
            while ((s = f.readLine()) != null) {
                stop.add(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("File not found, please check filepaths");
        }


        //stemming and stopword removal
        try (BufferedReader inputFileReader =
                     new BufferedReader(new FileReader(inputFile))) {
            String line = "";
            String ret = "";
            while ((line = inputFileReader.readLine()) != null) {
                for (String word : line.split(" ")) {
                    if (!stop.contains(word)) {
                        ret += parser.stemOfWord(word) + " ";
                    }
                }
                ret += "\n";
            }
            return ret;

        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }
}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;


public class ActualStemmer {
    /**
     * The program will be run using the following command:<p>
     * {@code Stemmer rulesfile inputfile}.
     */
    public static String StemmedText()

    {
        String ruleFilePath = "C:\\Users\\ruhit\\IdeaProjects\\Stemmer\\stem.rules";
        String inputFilePath = "C:\\Users\\ruhit\\IdeaProjects\\Stemmer\\a.txt";

        RuleFileParser parser = new RuleFileParser(ruleFilePath);

        File file = new File(inputFilePath);

        try (BufferedReader inputFileReader =
                     new BufferedReader(new FileReader(file))) {
            String line;
            String ret = "";
            while ((line = inputFileReader.readLine()) != null) {
//				System.out.println(line);
                for (String word : line.split("[\\s%,ঃ]+")) {
//					System.out.print(parser.stemOfWord(word) + " ");
                    ret += parser.stemOfWord(word) + " ";
                }
                return ret;
            }
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }
}

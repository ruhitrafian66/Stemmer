import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class RogueChecker {

    public void check(String testPath, String goldPath) {
        try {
            String[] testString = readText(testPath);
            String[] goldString = readText(goldPath);
//            int tsLength = testString.length;
//            int gLength = goldString.length;
//            for(String s: goldString){
////                if(s.equals(""))
////                    gLength--;
//                System.out.println(s);
//            }
// for(String s: testString){
////                if(s.equals(""))
////                    tsLength--;
//                System.out.println(s);
//            }
//            for(int c = 0; c<goldString.length;c++){
//                System.out.println(c+" " +goldString[c]);
//            }
//            for(int c = 0; c<testString.length;c++){
//                System.out.println(c+" " +testString[c]);
//            }

            double common = 0;
            for (String gs : goldString) {
                for (String ts : testString) {
//                    System.out.println(gs);
//                    System.out.println("Comparing with:");
//                    System.out.println(ts);
                    if (gs.equals(ts)) {
//                        System.out.println("Match!! Score is: " + common);
//                        System.out.println(ts);
                        common++;
                        break;
                    }
//                    System.out.println();
//                    System.out.println();
                }
            }
            System.out.println("Total sentences matched: " + common);
//            System.out.println(testString.length);
//            System.out.println(goldString.length);
            double precision = common / testString.length;
            double recall = common / goldString.length;
            double f1 = (Math.pow(precision, -1) + Math.pow(recall, -1)) / 2;
            System.out.println("Precision: " + precision);
            System.out.println("Recall: " + recall);
            System.out.println("F number: " + f1);
        } catch (Exception e) {

        }
    }

    public String[] readText(String path) {
        try {
            String temp = "";
            BufferedReader r = new BufferedReader(new FileReader(new File(path)));
            String line;
            while ((line = r.readLine()) != null) {
                temp += line;
            }
            String ret[] = temp.split("।");

            for (int c = 0; c < ret.length; c++) {
                ret[c] = ret[c].trim();
            }
//            System.out.println(temp);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

}

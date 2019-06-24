package menuFragments;

import java.util.ArrayList;
import java.util.List;

public class quizInfo {
    private static int count;
    public static List<String> answers = new ArrayList<String>();

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        quizInfo.count = count;
    }
}

package Sessions;

import java.util.ArrayList;
import java.util.List;

public class QuestionRecord {
    public String question;
    public List<String> alternatives = new ArrayList<>(4); // should contain 4
    public String pick; // selected alternative (can be null until picked)
    public boolean isCorrect;

    // default constructor required by LibGDX Json
    public QuestionRecord() {}

    public QuestionRecord(String question, List<String> alternatives, String pick, boolean isCorrect) {
        this.question = question;
        this.alternatives = alternatives;
        this.pick = pick;
        this.isCorrect = isCorrect;
    }
}

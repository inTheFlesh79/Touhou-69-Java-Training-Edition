package Sessions;

import java.util.ArrayList;
import java.util.List;

/**
 * One question record inside a TestRound.
 */
public class QuestionRecord {
    private String question;
    private List<String> alternatives = new ArrayList<>(4); // 4 alternatives expected
    private String pick; // selected alternative, nullable until chosen
    private boolean isCorrect;

    // required by LibGDX Json
    public QuestionRecord() {}

    public QuestionRecord(String question, List<String> alternatives) {
        this.question = question;
        if (alternatives != null) this.alternatives = alternatives;
    }

    public void setPick(String pick, boolean isCorrect) {
        this.pick = pick;
        this.setCorrect(isCorrect);
    }

	public boolean isCorrect() {
		return isCorrect;
	}

	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
    
    
}


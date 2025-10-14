package Sessions;

import java.util.ArrayList;
import java.util.List;

public class TestRound {
    public int gameId;
    public int testRoundId; // 1-based within the game session
    public List<QuestionRecord> questions = new ArrayList<>(); // expect 6 question records

    public TestRound() {}

    public TestRound(int gameId, int testRoundId) {
        this.gameId = gameId;
        this.testRoundId = testRoundId;
    }

    public void addQuestion(QuestionRecord q) {
        questions.add(q);
    }

    public int correctCount() {
        int c = 0;
        for (QuestionRecord q : questions) if (q.isCorrect) c++;
        return c;
    }
}


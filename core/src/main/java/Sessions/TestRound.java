package Sessions;

import java.util.ArrayList;
import java.util.List;

import Enemies.Pregunta;

/**
 * Represents one test round. When created during gameplay it may NOT have gameId/testRoundId.
 * Those IDs will be set when the session is finalized and persisted.
 */
public class TestRound {
    // nullable until finalize
    private Integer gameId;       // will be set when session is finalized
    private Integer testRoundId;  // will be set when session is finalized
    private ArrayList<Pregunta> questions = new ArrayList<>(); // expect 6 per spec

    // default constructor for Json
    public TestRound() {}

    // Optional convenience constructor
    public TestRound(ArrayList<Pregunta> questions) {
        if (questions != null) this.questions = questions;
    }

    public void addQuestion(Pregunta q) {
        questions.add(q);
    }

    public int correctCount() {
        int c = 0;
        for (Pregunta q : questions) if (q.getRespondidaCorrecta()) c++;
        return c;
    }

	public Integer getTestRoundId() {
		return testRoundId;
	}

	public void setTestRoundId(Integer testRoundId) {
		this.testRoundId = testRoundId;
	}

	public Integer getGameId() {
		return gameId;
	}

	public void setGameId(Integer gameId) {
		this.gameId = gameId;
	}
}



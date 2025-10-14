package Sessions;

import java.util.ArrayList;
import java.util.List;

public class GameSessionData {
    public int gameId;
    public String playerTag;
    public int score;
    public int testRounds; // number of test rounds played (can be kept in sync with rounds.size())
    public int finishedLevels;
    public String date; // yyyy-MM-dd
    public String time; // HH:mm
    public List<TestRound> rounds = new ArrayList<>();

    public GameSessionData() {}

    public GameSessionData(int gameId, String playerTag, String date, String time) {
        this.gameId = gameId;
        this.playerTag = playerTag;
        this.date = date;
        this.time = time;
        this.score = 0;
        this.testRounds = 0;
        this.finishedLevels = 0;
    }

    // convenience
    public void incrementScore(int delta) { score += delta; }
    public void finishLevel() { finishedLevels++; }
    public void addTestRound(TestRound round) {
        rounds.add(round);
        testRounds = rounds.size();
    }
}

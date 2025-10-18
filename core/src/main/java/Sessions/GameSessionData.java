package Sessions;

import java.util.ArrayList;
import java.util.List;

/**
 * Data recorded during a single play session.
 * gameId is assigned at startRecordingSession() (reserved) and finalized on save.
 */
public class GameSessionData {
    // These are nullable until set by manager
    private Integer gameId;     // persistent sequential id assigned at startRecordingSession()
    private String playerTag;   // set at finalize (player name)
    private String date;        // yyyy-MM-dd, set at finalize
    private String time;        // HH:mm, set at finalize

    // Live data recorded during the session
    private int score = 0;
    private int finishedLevels = 0;

    // number of test rounds can be derived from rounds.size()
    public List<TestRound> rounds = new ArrayList<>();

    public GameSessionData() {}

    // getters / setters
    public Integer getGameId() { return gameId; }
    public void setGameId(Integer gameId) { this.gameId = gameId; }

    public String getPlayerTag() { return playerTag; }
    public void setPlayerTag(String playerTag) { this.playerTag = playerTag; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public int getScore() { return score; }
    public void setScore(int newScore) { this.score = newScore; }
    public void addScore(int delta) { this.score += delta; }

    public int getFinishedLevels() { return finishedLevels; }
    public void setFinishedLevels(int l) { this.finishedLevels = l; }
    public void finishLevel() { this.finishedLevels++; }

    public void addTestRound(TestRound round) { rounds.add(round); }
    public int getTestRoundsCount() { return rounds.size(); }
    
    public List<TestRound> getRounds() { return rounds; }
    public void setRounds(List<TestRound> rounds) { this.rounds = rounds; }
    
}

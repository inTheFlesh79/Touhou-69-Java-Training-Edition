package Sessions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Singleton manager for session data.
 * Usage:
 *   SessionDataManager.getInstance().startNewSession("player1");
 *   SessionDataManager.getInstance().addScore(100);
 *   // when game ends:
 *   SessionDataManager.getInstance().saveCurrentSession(); // or closeCurrentSession();
 */
public class SessionDataManager {
    private static SessionDataManager instance;
    private static final String SESSIONS_FILENAME = "sessions.json";

    private SessionsArchive archive;
    private GameSessionData currentSession;
    private final Json json = new Json();

    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

    private SessionDataManager() {
        loadArchive();
    }

    public static SessionDataManager getInstance() {
        if (instance == null) {
            instance = new SessionDataManager();
        }
        return instance;
    }

    private FileHandle getFileHandle() {
        return Gdx.files.local(SESSIONS_FILENAME);
    }

    private void loadArchive() {
        FileHandle fh = getFileHandle();
        if (fh.exists()) {
            try {
                String content = fh.readString("UTF-8");
                archive = json.fromJson(SessionsArchive.class, content);
                if (archive == null) archive = new SessionsArchive();
            } catch (Exception e) {
                Gdx.app.error("SessionDataManager", "Failed to load sessions.json, creating new archive", e);
                archive = new SessionsArchive();
            }
        } else {
            archive = new SessionsArchive();
        }
    }

    private void saveArchive() {
        try {
            FileHandle fh = getFileHandle();
            String out = json.prettyPrint(archive);
            fh.writeString(out, false, "UTF-8"); // overwrite
        } catch (Exception e) {
            Gdx.app.error("SessionDataManager", "Failed to write sessions.json", e);
        }
    }

    // Lifecycle
    public GameSessionData startNewSession(String playerTag) {
        LocalDateTime now = LocalDateTime.now();
        int id = archive.nextGameId;
        String date = now.format(dateFmt);
        String time = now.format(timeFmt);

        currentSession = new GameSessionData(id, playerTag, date, time);
        // increment nextGameId for future sessions (persisted on save)
        archive.nextGameId = id + 1;
        return currentSession;
    }

    public Optional<GameSessionData> getCurrentSession() {
        return Optional.ofNullable(currentSession);
    }

    // Mutators used during gameplay
    public void addScore(int delta) {
        if (currentSession != null) currentSession.incrementScore(delta);
    }

    public void finishLevel() {
        if (currentSession != null) currentSession.finishLevel();
    }

    public void addTestRound(TestRound round) {
        if (currentSession != null) {
            currentSession.addTestRound(round);
        }
    }

    // Save current session into archive and write file (call at GameOver)
    public void saveCurrentSession() {
        if (currentSession == null) {
            Gdx.app.log("SessionDataManager", "No current session to save.");
            return;
        }
        // Add or replace existing session with same gameId (just in case)
        boolean replaced = false;
        for (int i = 0; i < archive.sessions.size(); i++) {
            if (archive.sessions.get(i).gameId == currentSession.gameId) {
                archive.sessions.set(i, currentSession);
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            archive.sessions.add(currentSession);
        }
        saveArchive();
        Gdx.app.log("SessionDataManager", "Saved session " + currentSession.gameId);
    }

    // Close session (save + drop currentSession from memory)
    public void closeCurrentSession() {
        saveCurrentSession();
        currentSession = null;
    }

    // Optional: load an existing session by ID (read-only)
    public GameSessionData loadSessionById(int gameId) {
        for (GameSessionData s : archive.sessions) {
            if (s.gameId == gameId) return s;
        }
        return null;
    }

    // Optional: get all saved sessions
    public java.util.List<GameSessionData> getAllSessions() {
        return archive.sessions;
    }
}
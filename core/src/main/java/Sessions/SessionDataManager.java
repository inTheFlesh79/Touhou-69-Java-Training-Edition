package Sessions;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Json.Serializer;

import Enemies.Pregunta;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * SessionDataManager - provisional ids (no reservation on start).
 * Saves an aggregated sessions file to local/sessions/sessions.json.
 */
public class SessionDataManager {
    private static SessionDataManager instance;

    // folder inside local/ (created automatically)
    private static final String SESSIONS_DIR = "sessions";
    private static final String SESSIONS_FILENAME = "sessions.json"; // final path: local/sessions/sessions.json

    // In-memory aggregated archive wrapper (persisted to disk)
    private static class SessionsArchive {
        public int nextGameId = 1;
        public List<GameSessionData> sessions = new ArrayList<>();
    }

    private SessionsArchive archive;
    private final Json json = new Json();

    // The live session that records while the player plays.
    private GameSessionData currentSession;

    // Date/time formatters
    private final DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

    private SessionDataManager() {
        loadArchive();

        // Register custom serializer for Pregunta so that all relevant fields are ALWAYS written/read.
        // This prevents LibGDX Json from producing ambiguous output (sometimes omitting fields).
        json.setSerializer(Pregunta.class, new Serializer<Pregunta>() {
            @Override
            public void write(Json json, Pregunta object, Class knownType) {
                json.writeObjectStart();
                // write everything except rutaImagen (per your request)
                json.writeValue("id", object.getID());
                json.writeValue("categoria", object.getCategoria());
                json.writeValue("enunciado", object.getEnunciado());
                // respuestas as array
                json.writeValue("respuestas", object.getRespuestas(), String[].class);
                json.writeValue("indiceCorrecto", object.getIndiceCorrecto());
                // ensure boolean is always written (default false if null)
                Boolean rc = object.getRespondidaCorrecta();
                json.writeValue("respondidaCorrecta", rc != null ? rc.booleanValue() : false);
                json.writeValue("indiceSeleccionado", object.getIndiceSeleccionado());
                json.writeObjectEnd();
            }

            @Override
            public Pregunta read(Json json, JsonValue jsonData, Class type) {
                // Read fields back and construct a Pregunta. Pregunta has a constructor matching these fields.
                try {
                    String id = jsonData.getString("id", null);
                    int categoria = jsonData.getInt("categoria", 0);
                    String enunciado = jsonData.getString("enunciado", null);

                    // read respuestas array
                    String[] respuestas = null;
                    JsonValue rv = jsonData.get("respuestas");
                    if (rv != null && rv.isArray()) {
                        List<String> tmp = new ArrayList<>();
                        for (JsonValue v : rv) {
                            tmp.add(v.asString());
                        }
                        respuestas = tmp.toArray(new String[0]);
                    } else {
                        respuestas = new String[0];
                    }

                    int indiceCorrecto = jsonData.getInt("indiceCorrecto", 0);
                    // create Pregunta with constructor (id, categoria, enunciado, respuestas, indiceCorrecto)
                    Pregunta p = new Pregunta(id, categoria, enunciado, respuestas, indiceCorrecto);

                    // set respondidaCorrecta if present
                    boolean rc = jsonData.getBoolean("respondidaCorrecta", false);
                    p.setRespondidaCorrecta(rc);
                    
                    if (jsonData.has("indiceSeleccionado"))
                        p.setIndiceSeleccionado(jsonData.getInt("indiceSeleccionado", -1));
                    
                    return p;
                } catch (Exception e) {
                    Gdx.app.error("SessionDataManager", "Failed to deserialize Pregunta from JSON; returning default Pregunta", e);
                    return new Pregunta(); // fallback empty Pregunta
                }
            }
        });
    }

    public static SessionDataManager getInstance() {
        if (instance == null) instance = new SessionDataManager();
        return instance;
    }

    // file handle for aggregated file inside local/sessions/
    private FileHandle getFileHandle() {
        return Gdx.files.local(SESSIONS_DIR + "/" + SESSIONS_FILENAME);
    }

    /**
     * Load sessions.json if present. If not present, create a fresh archive in memory.
     * Ensures the sessions directory exists on write.
     */
    private void loadArchive() {
        FileHandle fh = getFileHandle();
        if (fh.exists()) {
            try {
                String content = fh.readString("UTF-8");
                archive = json.fromJson(SessionsArchive.class, content);
                if (archive == null) archive = new SessionsArchive();
            } catch (Exception e) {
                Gdx.app.error("SessionDataManager", "Failed to load sessions.json; starting fresh.", e);
                archive = new SessionsArchive();
            }
        } else {
            archive = new SessionsArchive();
        }
    }

    /**
     * Persist archive to disk (overwrites sessions.json).
     * Ensures local/sessions/ folder exists.
     */
    private void saveArchive() {
        try {
            FileHandle fh = getFileHandle();
            // ensure parent dir exists
            FileHandle parent = fh.parent();
            if (!parent.exists()) parent.mkdirs();

            String out = json.prettyPrint(archive);
            fh.writeString(out, false, "UTF-8"); // overwrite file
            Gdx.app.log("SessionDataManager", "sessions.json written. nextGameId=" + archive.nextGameId +
                    "  (actual path: " + fh.file().getAbsolutePath() + ")");
        } catch (Exception e) {
            Gdx.app.error("SessionDataManager", "Failed to write sessions.json", e);
        }
    }

    // --------------------------
    // Public API for gameplay
    // --------------------------

    /**
     * Start an in-memory session and begin recording.
     * This is PROVISIONAL: it does NOT assign gameId. gameId will be assigned at finalizeAndSaveSession().
     * You can provide a playerTag now (so UI can ask for the username before entering the game).
     */
    public void startRecordingSession(String playerTag) {
        currentSession = new GameSessionData();
        if (playerTag != null) currentSession.setPlayerTag(playerTag);
        Gdx.app.log("SessionDataManager", "Started new provisional in-memory session (playerTag=" + playerTag + ")");
    }

    /**
     * Start a session without playerTag (overload).
     */
    public void startRecordingSession() {
        startRecordingSession(null);
    }

    /**
     * Returns the current recording session (could be null if not started).
     */
    public GameSessionData getCurrentSession() {
        return currentSession;
    }

    /**
     * Create a new TestRound object that you can fill. IDs are assigned at finalize.
     */
    public TestRound createNewTestRound() {
        return new TestRound();
    }

    public TestRound createNewTestRound(java.util.ArrayList<Pregunta> preguntasRonda) {
        return new TestRound(preguntasRonda);
    }

    /**
     * Add a finished TestRound to the current session (recorded silently).
     */
    public void addTestRound(TestRound round) {
        if (currentSession == null) {
            Gdx.app.error("SessionDataManager", "addTestRound called but no current session exists.");
            return;
        }
        currentSession.addTestRound(round);
    }

    /**
     * Increment score during gameplay.
     */
    public void addScore(int delta) {
        if (currentSession != null) currentSession.addScore(delta);
    }

    /**
     * Set the session score directly.
     */
    public void setScore(int newScore) {
        if (currentSession != null) currentSession.setScore(newScore);
    }

    /**
     * Mark a level finished during gameplay.
     */
    public void finishLevel() {
        if (currentSession != null) currentSession.finishLevel();
    }

    // --------------------------
    // Finalize & save at GameOver (assigns persistent gameId)
    // --------------------------

    /**
     * Finalize current in-memory session with playerTag and write to aggregated JSON.
     * This method:
     *   - assigns persistent gameId (archive.nextGameId)
     *   - assigns date/time (now)
     *   - assigns gameId & testRoundId for each TestRound
     *   - appends to archive.sessions and persists sessions.json
     * After calling this, currentSession is cleared from memory.
     */
    public void finalizeAndSaveSession(String playerTag) {
        if (currentSession == null) {
            Gdx.app.log("SessionDataManager", "No current session to finalize.");
            return;
        }

        // ensure archive is loaded
        if (archive == null) loadArchive();

        // assign persistent game id now (provisional approach: assign only once we save)
        int assignedGameId = archive.nextGameId;
        currentSession.setGameId(assignedGameId);
        archive.nextGameId = assignedGameId + 1; // increment for next save

        if (playerTag != null) currentSession.setPlayerTag(playerTag);

        // assign timestamp
        java.time.LocalDateTime now = java.time.LocalDateTime.now();
        currentSession.setDate(now.format(dateFmt));
        currentSession.setTime(now.format(timeFmt));

        // assign gameId and testRoundId to each round based on ordering (1-based)
        for (int i = 0; i < currentSession.rounds.size(); i++) {
            TestRound tr = currentSession.rounds.get(i);
            tr.setGameId(assignedGameId);
            tr.setTestRoundId(i + 1);
        }

        // add to archive (append)
        archive.sessions.add(currentSession);

        // write archive to disk
        saveArchive();

        // clear current session from memory
        currentSession = null;

        Gdx.app.log("SessionDataManager", "Finalized and saved session gameId=" + assignedGameId);
    }

    /**
     * If you decide NOT to save the session (e.g., user aborted), call this to discard the in-memory data.
     */
    public void discardCurrentSession() {
        currentSession = null;
        Gdx.app.log("SessionDataManager", "Discarded current in-memory session (no gameId was consumed).");
    }

    // --------------------------
    // Optional helpers to inspect archive
    // --------------------------

    /**
     * Return a read-only copy/reference to the list of saved sessions.
     */
    public List<GameSessionData> getAllSavedSessions() {
        if (archive == null) loadArchive();
        return archive.sessions;
    }

    /**
     * Load a saved session by gameId (returns null if not found).
     */
    public GameSessionData loadSavedSessionById(int gameId) {
        if (archive == null) loadArchive();
        for (GameSessionData s : archive.sessions) {
            if (s.getGameId() != null && s.getGameId() == gameId) return s;
        }
        return null;
    }
}
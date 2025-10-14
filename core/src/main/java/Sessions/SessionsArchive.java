package Sessions;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper stored in sessions.json:
 * {
 *   "nextGameId": 1,
 *   "sessions": [ ... ]
 * }
 */
public class SessionsArchive {
    public int nextGameId = 1; // starts at 1
    public List<GameSessionData> sessions = new ArrayList<>();

    public SessionsArchive() {}
}


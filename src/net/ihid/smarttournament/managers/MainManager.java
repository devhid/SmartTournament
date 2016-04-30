package net.ihid.smarttournament.managers;

import lombok.Getter;

/**
 * Created by Mikey on 4/29/2016.
 */
public class MainManager {
    @Getter
    private TournamentManager tournamentManager;

    @Getter
    private MatchManager matchManager;

    @Getter
    private ArenaManager arenaManager;

    public MainManager() {
        tournamentManager = new TournamentManager();
        matchManager = new MatchManager();
        arenaManager = new ArenaManager();
    }
}

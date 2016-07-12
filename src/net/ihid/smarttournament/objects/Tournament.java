package net.ihid.smarttournament.objects;

import lombok.Getter;
import lombok.Setter;
import net.ihid.smarttournament.api.TournamentAPI;
import net.ihid.smarttournament.TournamentPlugin;
import net.ihid.smarttournament.TournamentStage;
import net.ihid.smarttournament.player.SavedPlayerState;
import net.ihid.smarttournament.tasks.PreTournamentTask;
import net.ihid.smarttournament.tasks.TournamentTask;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;

/**
 * Created by Mikey on 4/24/2016.
 */
public class Tournament {
    @Getter @Setter
    private TournamentStage stage;

    @Getter @Setter
    private TournamentTask tournamentTask;

    @Getter
    private PreTournamentTask preTournamentTask;

    public Tournament() {
        this.stage = TournamentStage.NON_ACTIVE;
    }

    public void start() {
        reset(false);
        TournamentPlugin.getTournamentAPI().loadArenas();

        preTournamentTask = new PreTournamentTask(this);
        preTournamentTask.runTaskTimer(TournamentPlugin.getInstance(), 0L, 20L);
    }

    public void end() {
        if(preTournamentTask != null) {
            preTournamentTask.cancel();
        }

        if(tournamentTask != null) {
            tournamentTask.cancel();
        }

        reset(true);
        setStage(TournamentStage.NON_ACTIVE);
    }

    private void reset(boolean end) {
        final TournamentAPI tournamentAPI = TournamentPlugin.getTournamentAPI();
        Collection<? extends Player> online = Bukkit.getOnlinePlayers();

        if(end) {
            online.stream().filter(tournamentAPI::isInTournament).forEach(player -> {
                HashMap<Player, SavedPlayerState> playerStates = tournamentAPI.getPlayerStates();

                if(playerStates.containsKey(player)) {
                    playerStates.get(player).revert();
                    playerStates.remove(player);
                }

                player.teleport(tournamentAPI.getWorldSpawn());
            });
            tournamentAPI.clearParticipants();
        }

        tournamentAPI.clearWinners();
        tournamentAPI.clearMatches();
        tournamentAPI.clearArenas();
    }
}
